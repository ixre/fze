package net.fze.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
        mv = {1, 5, 1},
        k = 1,
        xi = 48,
        d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"},
        d2 = {"Lnet/fze/util/Strings;", "", "()V", "Companion", "fze-commons"}
)
public final class Strings {
    @NotNull
    public static final Strings.Companion Companion = new Strings.Companion((DefaultConstructorMarker)null);

    @JvmStatic
    public static final boolean isNullOrEmpty(@Nullable String s) {
        return Companion.isNullOrEmpty(s);
    }

    @JvmStatic
    @NotNull
    public static final String template(@NotNull String text, @NotNull Map args) {
        return Companion.template(text, args);
    }


    @JvmStatic
    @NotNull
    public static final String md5(@NotNull String str) {
        return Companion.md5(str);
    }

    @JvmStatic
    @NotNull
    public static final String shortMd5(@NotNull String str) {
        return Companion.shortMd5(str);
    }

    @JvmStatic
    @NotNull
    public static final String bytesToHex(@NotNull byte[] bytes) {
        return Companion.bytesToHex(bytes);
    }

    @JvmStatic
    @NotNull
    public static final byte[] encodeBase64(@NotNull byte[] bytes) {
        return Companion.encodeBase64(bytes);
    }

    @JvmStatic
    @NotNull
    public static final byte[] decodeBase64(@NotNull byte[] bytes) {
        return Companion.decodeBase64(bytes);
    }

    @JvmStatic
    @NotNull
    public static final String encodeBase64String(@NotNull byte[] bytes) {
        return Companion.encodeBase64String(bytes);
    }

    @JvmStatic
    @NotNull
    public static final byte[] decodeBase64String(@NotNull String s) {
        return Companion.decodeBase64String(s);
    }

    @Metadata(
            mv = {1, 5, 1},
            k = 1,
            xi = 48,
            d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\r\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010$\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J#\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\b2\u0006\u0010\t\u001a\u00020\nH\u0007¢\u0006\u0002\u0010\u000bJ\u0010\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u000eH\u0007J\u0010\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\r\u001a\u00020\u000eH\u0007J\u0010\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u0006H\u0007J\u0010\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\r\u001a\u00020\u000eH\u0007J\u0010\u0010\u0013\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u000eH\u0007J#\u0010\u0014\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00150\b2\u0006\u0010\t\u001a\u00020\nH\u0007¢\u0006\u0002\u0010\u0016J\u0018\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00182\u0006\u0010\t\u001a\u00020\nH\u0007J\u0012\u0010\u0019\u001a\u00020\u001a2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0006H\u0007J\u0010\u0010\u001b\u001a\u00020\u00062\u0006\u0010\u001c\u001a\u00020\u0006H\u0007J\u0010\u0010\u001d\u001a\u00020\u00062\u0006\u0010\u001c\u001a\u00020\u0006H\u0007J$\u0010\u001e\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00062\u0012\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060!H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\""},
            d2 = {"Lnet/fze/util/Strings$Companion;", "", "()V", "ext", "Lnet/fze/common/KotlinLangExtension;", "arrayJoin", "", "arr", "", "delimer", "", "([Ljava/lang/String;Ljava/lang/CharSequence;)Ljava/lang/String;", "bytesToHex", "bytes", "", "decodeBase64", "decodeBase64String", "s", "encodeBase64", "encodeBase64String", "intArrayJoin", "", "([Ljava/lang/Integer;Ljava/lang/CharSequence;)Ljava/lang/String;", "integerArrayJoin", "", "isNullOrEmpty", "", "md5", "str", "shortMd5", "template", "text", "args", "", "fze-commons"}
    )
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        public final boolean isNullOrEmpty(@Nullable String s) {
            boolean var10000;
            if (s != null) {
                boolean var3 = false;
                CharSequence var2 = (CharSequence)StringsKt.trim((CharSequence)s).toString();
                var3 = false;
                if (var2.length() != 0) {
                    var10000 = false;
                    return var10000;
                }
            }

            var10000 = true;
            return var10000;
        }

        @JvmStatic
        @NotNull
        public final String template(@NotNull String text, @NotNull Map args) {
            Intrinsics.checkNotNullParameter(text, "text");
            Intrinsics.checkNotNullParameter(args, "args");
            return Strings.template(text, args);
        }

      

        @JvmStatic
        @NotNull
        public final String md5(@NotNull String str) {
            Intrinsics.checkNotNullParameter(str, "str");
            String md5str = "";

            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                Charset var6 = Charsets.UTF_8;
                boolean var7 = false;
                byte[] var10000 = str.getBytes(var6);
                Intrinsics.checkNotNullExpressionValue(var10000, "(this as java.lang.String).getBytes(charset)");
                byte[] input = var10000;
                byte[] buff = md.digest(input);
                Intrinsics.checkNotNullExpressionValue(buff, "buff");
                md5str = this.bytesToHex(buff);
            } catch (Exception var8) {
                var8.printStackTrace();
            }

            return md5str;
        }

        @JvmStatic
        @NotNull
        public final String shortMd5(@NotNull String str) {
            Intrinsics.checkNotNullParameter(str, "str");
            String var2 = this.md5(str);
            byte var3 = 8;
            byte var4 = 24;
            boolean var5 = false;
            if (var2 == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            } else {
                String var10000 = var2.substring(var3, var4);
                Intrinsics.checkNotNullExpressionValue(var10000, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                return var10000;
            }
        }

        @JvmStatic
        @NotNull
        public final String bytesToHex(@NotNull byte[] bytes) {
            Intrinsics.checkNotNullParameter(bytes, "bytes");
            StringBuffer md5str = new StringBuffer();
            int digital = 0;
            int var4 = 0;
            int var5 = bytes.length + -1;
            if (var4 <= var5) {
                do {
                    int i = var4++;
                     digital = bytes[i];
                    if (digital < 0) {
                        digital += 256;
                    }

                    if (digital < 16) {
                        md5str.append("0");
                    }

                    md5str.append(Integer.toHexString(digital));
                } while(var4 <= var5);
            }

            String var8 = md5str.toString();
            Intrinsics.checkNotNullExpressionValue(var8, "md5str.toString()");
            boolean var9 = false;
            String var10000 = var8.toUpperCase();
            Intrinsics.checkNotNullExpressionValue(var10000, "(this as java.lang.String).toUpperCase()");
            return var10000;
        }

        @JvmStatic
        @NotNull
        public final byte[] encodeBase64(@NotNull byte[] bytes) {
            Intrinsics.checkNotNullParameter(bytes, "bytes");
            byte[] var2 = Base64.getEncoder().encode(bytes);
            Intrinsics.checkNotNullExpressionValue(var2, "getEncoder().encode(bytes)");
            return var2;
        }

        @JvmStatic
        @NotNull
        public final byte[] decodeBase64(@NotNull byte[] bytes) {
            Intrinsics.checkNotNullParameter(bytes, "bytes");
            byte[] var2 = Base64.getDecoder().decode(bytes);
            Intrinsics.checkNotNullExpressionValue(var2, "getDecoder().decode(bytes)");
            return var2;
        }

        @JvmStatic
        @NotNull
        public final String encodeBase64String(@NotNull byte[] bytes) {
            Intrinsics.checkNotNullParameter(bytes, "bytes");
            String var2 = Base64.getEncoder().encodeToString(bytes);
            Intrinsics.checkNotNullExpressionValue(var2, "getEncoder().encodeToString(bytes)");
            return var2;
        }

        @JvmStatic
        @NotNull
        public final byte[] decodeBase64String(@NotNull String s) {
            Intrinsics.checkNotNullParameter(s, "s");
            byte[] var2 = Base64.getDecoder().decode(s);
            Intrinsics.checkNotNullExpressionValue(var2, "getDecoder().decode(s)");
            return var2;
        }

        // $FF: synthetic method
        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
