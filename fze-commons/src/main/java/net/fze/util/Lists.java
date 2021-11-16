package net.fze.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
        mv = {1, 5, 1},
        k = 1,
        xi = 48,
        d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"},
        d2 = {"Lnet/fze/util/Lists;", "", "()V", "Companion", "fze-commons"}
)
public final class Lists {
    @NotNull
    public static final Lists.Companion Companion = new Lists.Companion((DefaultConstructorMarker)null);

    @JvmStatic
    @NotNull
    public static final List create() {
        return Companion.create();
    }

    @JvmStatic
    @NotNull
    public static final List of(@NotNull Object... args) {
        return Companion.of(args);
    }

    @JvmStatic
    @NotNull
    public static final List sort(@NotNull List list, @NotNull Comparator c) {
        return Companion.sort(list, c);
    }

    @JvmStatic
    public static final void reverse(@NotNull List list) {
        Companion.reverse(list);
    }

    @JvmStatic
    @NotNull
    public static final List sortByDescending(@NotNull List list, @NotNull Comparator c) {
        return Companion.sortByDescending(list, c);
    }

    @Metadata(
            mv = {1, 5, 1},
            k = 1,
            xi = 48,
            d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\u0004\b\u0000\u0010\u0005H\u0007J-\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\u0004\b\u0000\u0010\u00052\u0012\u0010\u0007\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00050\b\"\u0002H\u0005H\u0007¢\u0006\u0002\u0010\tJ\u001c\u0010\n\u001a\u00020\u000b\"\u0004\b\u0000\u0010\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\f0\u000eH\u0007J2\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\f0\u000e\"\u0004\b\u0000\u0010\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\f0\u000e2\u000e\u0010\u0010\u001a\n\u0012\u0006\b\u0000\u0012\u0002H\f0\u0011H\u0007J2\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\f0\u000e\"\u0004\b\u0000\u0010\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\f0\u000e2\u000e\u0010\u0010\u001a\n\u0012\u0006\b\u0000\u0012\u0002H\f0\u0011H\u0007¨\u0006\u0013"},
            d2 = {"Lnet/fze/util/Lists$Companion;", "", "()V", "create", "", "E", "of", "args", "", "([Ljava/lang/Object;)Ljava/util/List;", "reverse", "", "T", "list", "", "sort", "c", "Ljava/util/Comparator;", "sortByDescending", "fze-commons"}
    )
    public static final class Companion {
        private Companion() {
        }

        @JvmStatic
        @NotNull
        public final List create() {
            boolean var1 = false;
            return (List)(new ArrayList());
        }

        @JvmStatic
        @NotNull
        public final List of(@NotNull Object... args) {
            Intrinsics.checkNotNullParameter(args, "args");
            boolean var3 = false;
            List list = (List)(new ArrayList());
            CollectionsKt.addAll((Collection)list, args);
            return list;
        }

        @JvmStatic
        @NotNull
        public final List sort(@NotNull List list, @NotNull Comparator c) {
            Intrinsics.checkNotNullParameter(list, "list");
            Intrinsics.checkNotNullParameter(c, "c");
            return CollectionsKt.sortedWith((Iterable)list, c);
        }

        @JvmStatic
        public final void reverse(@NotNull List list) {
            Intrinsics.checkNotNullParameter(list, "list");
            Collections.reverse(list);
        }

        @JvmStatic
        @NotNull
        public final List sortByDescending(@NotNull List list, @NotNull Comparator c) {
            Intrinsics.checkNotNullParameter(list, "list");
            Intrinsics.checkNotNullParameter(c, "c");
            List dst = this.sort(list, c);
            this.reverse(dst);
            return dst;
        }

        // $FF: synthetic method
        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}
