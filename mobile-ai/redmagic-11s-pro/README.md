# REDMAGIC 11S Pro mobile AI lab

Michael Insch · 23 November 2026 · Physical Android handset, NX809J

I built the Android software that connects a local model, a media generator and the apps I use to develop on the phone. The most recent addition is **Fielddesk**, a configurable Android home screen. The strongest measured media result is a text-to-image run through FR3K Forge on the handset.

[Android source](fielddesk/Desk.java) · [Machine-readable results](results.json) · [Profile validation cases](fielddesk/tests/ProfileCases.java)

## From prompt to pixels

I asked for **“FR3K field robot under neon lights.”** Forge called the separately installed Nightmare Mobile service on the phone. AbsoluteReality generated a 512 × 512 RGB PNG in eight steps with seed 42. Rendering and local retrieval together took **4.831 seconds** for this single run. A second test reopened those same bytes in Forge. A subsequent force-stop and restart tested whether the saved image survived and reopened from the gallery.

The file was 430,519 bytes. Its SHA-256 is in the JSON record. The test invoked Forge’s Android client through instrumentation. A fresh tap-through of the complete render dialog is still an open UI test.

## What ran on the phone

| Workload | Evidence | Result |
| --- | --- | --- |
| Qwen3.6-35B-A3B in Geode | Operator-reported use | I reported running the model on the handset. Geode’s hardware report projected 36.87 tokens/s for overlapped decode. Its settings identify a Gemma model while its estimator names Qwen. Actual 35B throughput and backend assignment remain unmeasured in this record. |
| Qwen3.5-4B CPU reference | Handset benchmark | Four threads, Q4_K_M, 36.32 tokens/s for a 512-token prompt and 12.81 tokens/s for 128-token generation. This is a separate model and run. |
| Image generation | Physical inference and output validation | Nightmare Mobile 1.6.307 generated the PNG above. Forge handled the request, retrieval, preview and persistent gallery. |
| Video | Physical playback and export | Android VideoView decoded a four-second 320 × 180 H.264 fixture. The exported copy matched the input bytes. Generating a new video from a prompt on this handset remains an open test. |
| Mobile developer tools | Physical service checks | Three native MCP services exposed Termux, filesystem and sensors on handset loopback. Tests exercised initialisation, authenticated tool discovery and rejection of invalid credentials. |

## Fielddesk source • 23 November

[Desk.java](fielddesk/Desk.java) discovers a model through a loopback HTTP endpoint, requests a chat completion and displays the serving model ID beside the answer. Separate shortcuts let the user pick apps for Geode, Forge and Termux. A versioned JSON profile controls the title, theme, prompt and workflows. Invalid profiles are rejected before activation, and the previous profile can be restored.

The source snapshot is version 0.2. The earlier 0.1 debug APK passed build and signing checks. Physical acceptance of the 0.2 launcher and its end-to-end chat handoff is pending. To build the source, use JDK, Android SDK platform 36, Build Tools 36.0.0, zip and a local Android debug keystore. Run `bash fielddesk/build.sh` from this directory. [Example profile](fielddesk/profiles/redmagic.json) · [Manifest](fielddesk/AndroidManifest.xml)

## Next measurements

A useful 35B trace pairs the active model identity with a new prompt and reply, generated-token count, time to first token, elapsed decode time and backend. A useful video result pairs the model and prompt with a newly generated MP4, duration, codec, elapsed time and output hash. The public JSON keeps both outcomes explicit so a later run can be compared with this snapshot.
