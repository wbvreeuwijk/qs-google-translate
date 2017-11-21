package net.reeuwijk.qlik.aai.util;

import java.util.logging.Logger;

import qlik.sse.BundledRows;
import qlik.sse.DataType;
import qlik.sse.FunctionType;
import qlik.sse.Row;

public interface SSEFunction {
	
	static final Logger logger = Logger.getLogger(SSEFunction.class.getName());
	
	
	public static String getName() {
		return null;
	};
	
	public static String getParameters() {
		return null;
	};
	
	public static DataType getReturnType() {
		return DataType.UNRECOGNIZED;
	}
	public static FunctionType getFunctionType() {
		return FunctionType.UNRECOGNIZED;
	}

	public void executeRow(Row inRow);

	public BundledRows getReturnRows();
	
}
