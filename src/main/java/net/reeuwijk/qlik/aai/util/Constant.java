package net.reeuwijk.qlik.aai.util;

import static io.grpc.Metadata.BINARY_BYTE_MARSHALLER;

import io.grpc.Context;
import io.grpc.Metadata;
import io.grpc.Metadata.Key;
import qlik.sse.CommonRequestHeader;
import qlik.sse.FunctionRequestHeader;
import qlik.sse.ScriptRequestHeader;

public class Constant {
	public static final Context.Key<ScriptRequestHeader> SCRIPT_HEADER = Context.key("qlik-scriptrequestheader-bin");
	public static final Context.Key<FunctionRequestHeader> FUNCTION_HEADER = Context.key("qlik-functionrequestheader-bin");
	public static final Context.Key<CommonRequestHeader> COMMON_HEADER = Context.key("qlik-scriptrequestheader-bin");

	public static final Key<byte[]> SCRIPT_REQUEST_HEADER = Metadata.Key.of("qlik-scriptrequestheader-bin",
			BINARY_BYTE_MARSHALLER);
	public static final Key<byte[]> FUNCTION_REQUEST_HEADER = Metadata.Key.of("qlik-functionrequestheader-bin",
			BINARY_BYTE_MARSHALLER);
	public static final Key<byte[]> COMMON_REQUEST_HEADER = Metadata.Key.of("qlik-commonrequestheader-bin",
			BINARY_BYTE_MARSHALLER);

}
