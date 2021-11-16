package net.fze.common;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URL;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;

@Metadata(
    mv = {1, 5, 1},
    k = 1,
    xi = 48,
    d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0002\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"},
    d2 = {"Lnet/fze/common/StandardKt;", "", "()V", "Companion", "fze-commons"}
)
final class StandardKt {
    @NotNull
    public static final StandardKt.Companion Companion = new StandardKt.Companion((DefaultConstructorMarker)null);
    private static int devFlag = -1;
    private static final boolean java9OrLater;

    public StandardKt() {
    }



    @JvmStatic
    public static final void setDefaultExceptionHandler(@NotNull UncaughtExceptionHandler handler) {
        Companion.setDefaultExceptionHandler(handler);
    }

    static {
        java9OrLater = Companion.detectIsJava9OrLater();
    }

    @Metadata(
        mv = {1, 5, 1},
        k = 1,
        xi = 48,
        d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0014\u0010\u0007\u001a\u00020\u00062\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\tH\u0007J\b\u0010\n\u001a\u00020\u0006H\u0002J\b\u0010\u000b\u001a\u00020\u0006H\u0007J\b\u0010\f\u001a\u00020\u0006H\u0007J\u0014\u0010\r\u001a\u00020\u00062\n\u0010\u000e\u001a\u0006\u0012\u0002\b\u00030\tH\u0007J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"},
        d2 = {"Lnet/fze/common/StandardKt$Companion;", "", "()V", "devFlag", "", "java9OrLater", "", "classInJar", "c", "Ljava/lang/Class;", "detectIsJava9OrLater", "dev", "isJava9OrLater", "resolveEnvironment", "main", "setDefaultExceptionHandler", "", "handler", "Ljava/lang/Thread$UncaughtExceptionHandler;", "fze-commons"}
    )
    public static final class Companion {
        private Companion() {
        }

        private final boolean detectIsJava9OrLater() {
            String version = System.getProperty("java.version");
            Regex var10000 = new Regex("^\\d+$");
            Intrinsics.checkNotNullExpressionValue(version, "version");
            if (var10000.matches((CharSequence)version)) {
                boolean var3 = false;
                return Integer.parseInt(version) >= 9;
            } else {
                return false;
            }
        }


        @JvmStatic
        public final void setDefaultExceptionHandler(@NotNull UncaughtExceptionHandler handler) {
            Intrinsics.checkNotNullParameter(handler, "handler");
            Thread.setDefaultUncaughtExceptionHandler(handler);
        }

        // $FF: synthetic method
        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
