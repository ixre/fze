package net.fze.ext.grpc;

import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;

/**
 * Grpc工具
 */
public class GrpcUtil {
        /** 从JSON中转换 */
        public static Message fromJson(Message.Builder builder, String json) throws Exception {
            JsonFormat.parser().merge(json, builder);
            return builder.build();
        }
        /** 转换为JSON */
        public static String toJson(MessageOrBuilder message) throws Exception {
            return JsonFormat.printer().print(message);
        }
    }
