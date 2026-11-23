package dev.fr3k.fielddesk;

import android.app.*;
import android.os.Bundle;
import android.content.*;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.provider.Settings;
import android.view.WindowInsets;
import android.widget.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import org.json.*;

public class Desk extends Activity {
    private LinearLayout panel;
    private final String[] roles = {"Ask · Geode", "Create · Forge", "Operate · Termux"};

    private static final String DEFAULT_PROFILE = "{\"schema\":1,\"title\":\"FR3K / Fielddesk\",\"accent\":\"#88FFD2\",\"systemPrompt\":\"\",\"workflows\":[]}";
    private JSONObject profile;
    private boolean asking;
    private TextView answer;
    private EditText question;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        try {
            profile = parseProfile(getPreferences(0).getString("profile.active", DEFAULT_PROFILE));
        } catch (JSONException invalid) {
            try { profile = parseProfile(DEFAULT_PROFILE); }
            catch (JSONException impossible) { throw new IllegalStateException(impossible); }
        }
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(0xff101816);
        panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        int pad = Math.round(20 * getResources().getDisplayMetrics().density);
        scroll.setOnApplyWindowInsetsListener((view, insets) -> {
            view.setPadding(pad, pad + insets.getSystemWindowInsetTop(), pad, pad + insets.getSystemWindowInsetBottom());
            return insets;
        });
        scroll.addView(panel);
        setContentView(scroll);
        TextView title = new TextView(this);
        title.setText(profile.optString("title"));
        title.setTextSize(29);
        title.setTextColor(Color.parseColor(profile.optString("accent")));
        panel.addView(title);
        question = new EditText(this);
        question.setHint("Ask the local model");
        question.setMaxLines(4);
        panel.addView(question);
        button("Ask on this phone", this::ask);
        answer = new TextView(this);
        answer.setTextIsSelectable(true);
        panel.addView(answer);
        JSONArray workflows = profile.optJSONArray("workflows");
        for (int i = 0; i < workflows.length(); i++) {
            JSONObject flow = workflows.optJSONObject(i);
            button(flow.optString("label"), () -> {
                question.setText(flow.optString("prompt"));
                question.setSelection(question.length());
                question.requestFocus();
            });
        }
        button("Edit profile", this::editProfile);
        if (getPreferences(0).contains("profile.previous"))
            button("Previous profile", () -> new AlertDialog.Builder(this)
                .setMessage("Restore the previous profile?")
                .setPositiveButton("Restore", (d, w) -> activate(getPreferences(0)
                    .getString("profile.previous", DEFAULT_PROFILE)))
                .setNegativeButton("Cancel", null).show());
        for (String role : roles) {
            Button b = button(role, () -> useRole(role));
            b.setOnLongClickListener(v -> { choose(role); return true; });
        }
        button("All apps", () -> choose(null));
        button("Configure shortcuts", () -> new AlertDialog.Builder(this)
            .setTitle("Choose shortcut").setItems(roles, (d, i) -> choose(roles[i])).show());
        button("Home app settings", () -> open(new Intent(Settings.ACTION_HOME_SETTINGS)));
    }

    // The previous handset runbook observed this listener. Discover the served model anew.
    private JSONObject request(String path, JSONObject body) throws Exception {
        URL url = new URL("http://127.0.0.1:18081/v1/" + path);
        HttpURLConnection c = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
        try {
            c.setInstanceFollowRedirects(false);
            c.setConnectTimeout(3000);
            c.setReadTimeout(90000);
            if (body != null) {
                c.setRequestMethod("POST");
                c.setDoOutput(true);
                c.setRequestProperty("Content-Type", "application/json");
                try (OutputStream out = c.getOutputStream()) {
                    out.write(body.toString().getBytes(StandardCharsets.UTF_8));
                }
            }
            int status = c.getResponseCode();
            if (status != 200) throw new IOException("Local server returned HTTP " + status);
            try (InputStream in = c.getInputStream(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                byte[] buf = new byte[4096];
                int n;
                while ((n = in.read(buf)) != -1) {
                    if (out.size() + n > 262144) throw new IOException("Response too large");
                    out.write(buf, 0, n);
                }
                return new JSONObject(out.toString("UTF-8"));
            }
        } finally { c.disconnect(); }
    }

    private void ask() {
        String prompt = question.getText().toString().trim();
        if (asking || prompt.isEmpty()) return;
        if (prompt.length() > 8192) { question.setError("Limit 8192 characters"); return; }
        asking = true;
        answer.setText("Contacting local model…");
        new Thread(() -> {
            String display;
            try {
                String model = request("models", null).getJSONArray("data").getJSONObject(0).getString("id");
                JSONObject msg = new JSONObject().put("role", "user").put("content", prompt);
                JSONArray messages = new JSONArray();
                String system = profile.optString("systemPrompt");
                if (!system.isEmpty())
                    messages.put(new JSONObject().put("role", "system").put("content", system));
                messages.put(msg);
                JSONObject payload = new JSONObject().put("model", model)
                    .put("messages", messages).put("max_tokens", 256);
                JSONObject reply = request("chat/completions", payload).getJSONArray("choices").getJSONObject(0);
                String content = reply.getJSONObject("message").getString("content").trim();
                if (content.isEmpty()) throw new IOException("Empty model reply");
                display = model + "\n\n" + content;
            } catch (Exception ex) {
                display = "Local chat unavailable (" + ex.getClass().getSimpleName() +
                    "). Check the model server on port 18081, or open Geode below.";
            }
            final String result = display;
            runOnUiThread(() -> {
                asking = false;
                if (!isDestroyed()) answer.setText(result);
            });
        }, "fielddesk-local-chat").start();
    }

    static JSONObject parseProfile(String raw) throws JSONException {
        if (raw.length() > 65536) throw new JSONException("Profile exceeds 65536 characters");
        JSONTokener parser = new JSONTokener(raw);
        Object root = parser.nextValue();
        if (!(root instanceof JSONObject) || parser.nextClean() != 0)
            throw new JSONException("Expected one JSON object");
        JSONObject p = (JSONObject) root;
        if (p.length() != 5 || !(p.get("schema") instanceof Integer) || p.getInt("schema") != 1)
            throw new JSONException("Expected profile schema 1 and its five fields");
        field(p, "title", 1, 48);
        if (!field(p, "accent", 7, 7).matches("#[0-9A-Fa-f]{6}"))
            throw new JSONException("Use a six-digit hex colour");
        field(p, "systemPrompt", 0, 2048);
        JSONArray flows = p.getJSONArray("workflows");
        if (flows.length() > 12) throw new JSONException("Maximum 12 workflows");
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < flows.length(); i++) {
            JSONObject flow = flows.getJSONObject(i);
            String id = field(flow, "id", 1, 32);
            if (flow.length() != 3 || !id.matches("[a-z][a-z0-9-]{0,31}") || !ids.add(id))
                throw new JSONException("Invalid or duplicate workflow ID: " + id);
            field(flow, "label", 1, 48);
            field(flow, "prompt", 1, 2048);
        }
        return p;
    }

    private static String field(JSONObject object, String key, int min, int max) throws JSONException {
        Object value = object.get(key);
        if (!(value instanceof String)) throw new JSONException("Expected text for " + key);
        String s = (String) value;
        if (s.trim().length() < min || s.length() > max) throw new JSONException("Invalid length for " + key);
        return s;
    }

    private void editProfile() {
        EditText editor = new EditText(this);
        editor.setMinLines(5);
        editor.setMaxLines(12);
        try { editor.setText(profile.toString(2)); }
        catch (JSONException invalid) { editor.setText(DEFAULT_PROFILE); }
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Edit profile")
            .setView(editor).setPositiveButton("Apply", null).setNegativeButton("Cancel", null).create();
        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            try {
                String candidate = parseProfile(editor.getText().toString()).toString();
                dialog.dismiss();
                activate(candidate);
            } catch (JSONException invalid) { editor.setError(invalid.getMessage()); }
        }));
        dialog.show();
    }

    private void activate(String candidate) {
        if (asking) {
            Toast.makeText(this, "Wait for the current reply before changing profile", Toast.LENGTH_LONG).show();
            return;
        }
        if (candidate.equals(profile.toString())) return;
        boolean saved = getPreferences(0).edit().putString("profile.previous", profile.toString())
            .putString("profile.active", candidate).commit();
        if (saved) recreate();
        else Toast.makeText(this, "Profile could not be saved", Toast.LENGTH_LONG).show();
    }

    private Button button(String label, Runnable action) {
        Button b = new Button(this);
        b.setText(label);
        b.setAllCaps(false);
        b.setOnClickListener(v -> action.run());
        panel.addView(b);
        return b;
    }

    private void useRole(String role) {
        String saved = getPreferences(0).getString(role, "");
        ComponentName component = ComponentName.unflattenFromString(saved);
        if (component == null || !open(Intent.makeMainActivity(component))) choose(role);
    }

    private boolean open(Intent intent) {
        try { startActivity(intent); return true; }
        catch (ActivityNotFoundException | SecurityException ex) {
            Toast.makeText(this, "App unavailable", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void choose(String role) {
        Intent launcher = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> entries = new ArrayList<>(getPackageManager().queryIntentActivities(launcher, 0));
        entries.removeIf(e -> e.activityInfo.packageName.equals(getPackageName()));
        entries.sort(new ResolveInfo.DisplayNameComparator(getPackageManager()));
        String[] labels = new String[entries.size()];
        for (int i = 0; i < labels.length; i++) {
            ResolveInfo e = entries.get(i);
            labels[i] = e.loadLabel(getPackageManager()) + "\n" + e.activityInfo.packageName;
        }
        new AlertDialog.Builder(this).setTitle(role == null ? "All apps" : "Choose app for " + role)
            .setItems(labels, (dialog, index) -> {
                ResolveInfo e = entries.get(index);
                ComponentName c = new ComponentName(e.activityInfo.packageName, e.activityInfo.name);
                if (open(Intent.makeMainActivity(c)) && role != null)
                    getPreferences(0).edit().putString(role, c.flattenToString()).apply();
            }).show();
    }
}