package net.reeuwijk.qlik.aai.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.grpc.stub.StreamObserver;
import qlik.sse.BundledRows;
import qlik.sse.Capabilities;
import qlik.sse.ConnectorGrpc;
import qlik.sse.DataType;
import qlik.sse.FunctionDefinition;
import qlik.sse.FunctionType;
import qlik.sse.Parameter;
import qlik.sse.Row;

public class SSEServer extends ConnectorGrpc.ConnectorImplBase {

	private static final String PLUGIN_VERSION = "0.1";
	private static final String PLUGIN_ID = "aai-java-regexp";
	private ArrayList<Class<SSEFunction>> sseFunctions = new ArrayList<Class<SSEFunction>>();

	static final Logger logger = Logger.getLogger(SSEServer.class.getName());

	public void registerFunction(Class<SSEFunction> sseFunction) {
		sseFunctions.add(sseFunction);
	}

	@Override
	public void getCapabilities(qlik.sse.Empty request,
			io.grpc.stub.StreamObserver<qlik.sse.Capabilities> responseObserver) {
		logger.info("Received getCapabilities");
		qlik.sse.Capabilities.Builder capabilitiesBuilder = Capabilities.newBuilder();
		capabilitiesBuilder = capabilitiesBuilder.setAllowScript(false).setPluginVersion(PLUGIN_VERSION)
				.setPluginIdentifier(PLUGIN_ID);
		for (int i = 0; i < sseFunctions.size(); i++) {
			qlik.sse.FunctionDefinition.Builder fb = FunctionDefinition.newBuilder();
			Class<SSEFunction> cls = sseFunctions.get(i);
			Method m;
			try {
				m = cls.getMethod("getName", new Class<?>[0]);
				fb = fb.setName((String) m.invoke(null, new Object[0]));
				m = cls.getMethod("getFunctionType", new Class<?>[0]);
				fb = fb.setFunctionType((FunctionType) m.invoke(null, new Object[0]));
				m = cls.getMethod("getReturnType", new Class<?>[0]);
				fb = fb.setReturnType((DataType) m.invoke(null, new Object[0])).setFunctionId(i);
				m = cls.getMethod("getParameters", new Class<?>[0]);
				String parameters = (String) m.invoke(null, new Object[0]);
				String[] parameter = parameters.split(",");
				for (int j = 0; j < parameter.length; j++) {
					String[] items = parameter[j].split(":");
					qlik.sse.Parameter.Builder pb = Parameter.newBuilder();
					pb = pb.setName(items[0]);
					if (items[1].equals("String")) {
						pb = pb.setDataType(DataType.STRING);
					} else if (items[1].equals("Numeric")) {
						pb = pb.setDataType(DataType.NUMERIC);
					} else if (items[1].equals("Dual")) {
						pb = pb.setDataType(DataType.DUAL);
					} else {
						pb = pb.setDataType(DataType.UNRECOGNIZED);
					}
					fb = fb.addParams(pb.build());
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			capabilitiesBuilder = capabilitiesBuilder.addFunctions(fb.build());
		}
		responseObserver.onNext(capabilitiesBuilder.build());
		responseObserver.onCompleted();
	}

	public io.grpc.stub.StreamObserver<qlik.sse.BundledRows> executeFunction(
			io.grpc.stub.StreamObserver<qlik.sse.BundledRows> responseObserver) {

		int functionId = Constant.FUNCTION_HEADER.get().getFunctionId();

		Class<SSEFunction> cls = sseFunctions.get(functionId);
		SSEFunction sseFunction;
		try {
			sseFunction = cls.newInstance();
			logger.info("Executing function: "+sseFunction.getClass().getName());
			StreamObserver<BundledRows> response = new StreamObserver<BundledRows>() {
				@Override
				public void onNext(BundledRows value) {
					List<Row> inputRows = value.getRowsList();
					for (Row inRow : inputRows) {
						sseFunction.executeRow(inRow);
					}
					responseObserver.onNext(sseFunction.getReturnRows());
				}

				@Override
				public void onError(Throwable t) {
					logger.log(Level.WARNING, "Encountered Error somewhere", t);
				}

				@Override
				public void onCompleted() {
					responseObserver.onCompleted();
				}
			};
			return response;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseObserver;
	}
}