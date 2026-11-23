package dev.fr3k.fielddesk;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import org.json.*;

public class ProfileCases {
    private static void rejected(String input) throws Exception {
        try { Desk.parseProfile(input); }
        catch (JSONException expected) { return; }
        throw new AssertionError("Invalid profile accepted");
    }
    public static void main(String[] args) throws Exception {
        String raw = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);
        JSONObject valid = Desk.parseProfile(raw);
        Desk.parseProfile(valid.toString());
        rejected("{}");
        rejected(raw + "x");
        rejected(new JSONObject(raw).put("schema", 2).toString());
        rejected(new JSONObject(raw).put("extra", true).toString());
        rejected(new JSONObject(raw).put("accent", "green").toString());
        rejected(new JSONObject(raw).put("systemPrompt", new String(new char[2049])).toString());
        JSONArray flows = valid.getJSONArray("workflows");
        flows.put(flows.getJSONObject(0));
        rejected(valid.toString());
        System.out.println("Valid profile and round trip passed; seven invalid cases rejected.");
    }
}
