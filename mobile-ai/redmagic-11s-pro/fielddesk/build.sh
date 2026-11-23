#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
sdk=${ANDROID_HOME:-$HOME/Android/Sdk}
bt="$sdk/build-tools/36.0.0"
jarfile="$sdk/platforms/android-36/android.jar"
work=$(mktemp -d)
trap 'rm -rf "$work"' EXIT
mkdir -p "$work/classes" "$work/dex"
javac --release 8 -classpath "$jarfile" -d "$work/classes" Desk.java
jar cf "$work/app.jar" -C "$work/classes" .
"$bt/d8" --min-api 26 --lib "$jarfile" --output "$work/dex" "$work/app.jar"
"$bt/aapt2" link -I "$jarfile" --manifest AndroidManifest.xml -o "$work/base.apk"
(cd "$work/dex" && zip -q "$work/base.apk" classes.dex)
"$bt/zipalign" -f 4 "$work/base.apk" "$work/aligned.apk"
: "${FIELDESK_KEYSTORE:=$HOME/.android/debug.keystore}"
"$bt/apksigner" sign --ks "$FIELDESK_KEYSTORE" --ks-pass pass:android --key-pass pass:android --ks-key-alias androiddebugkey --out "$work/fielddesk-debug.apk" "$work/aligned.apk"
"$bt/apksigner" verify --verbose "$work/fielddesk-debug.apk"
cp "$work/fielddesk-debug.apk" fielddesk-debug.apk
sha256sum fielddesk-debug.apk
