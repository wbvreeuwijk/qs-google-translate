package net.reeuwijk.qlik.aai.functions;

import java.util.List;

import net.reeuwijk.qlik.aai.util.SSEFunction;
import net.reeuwijk.qlik.translate.util.Dictionary;
import qlik.sse.BundledRows;
import qlik.sse.BundledRows.Builder;
import qlik.sse.DataType;
import qlik.sse.Dual;
import qlik.sse.FunctionType;
import qlik.sse.Row;


public class TranslateRowsFunction implements SSEFunction {
	
	private Builder returnRowsBuilder = BundledRows.newBuilder();
	
	private static final String FUNCTION_NAME = "TranslateRows";
	
	public static String getParameters() {
		return "Language:String,Text:String";
	}
	
	public static DataType getReturnType() {
		return DataType.STRING;
	}
	
	public static FunctionType getFunctionType() {
		return FunctionType.TENSOR;
	}

	public static String getName() {
		return FUNCTION_NAME;
	}

	@Override
	public void executeRow(Row inRow) {
		List<Dual> list = inRow.getDualsList();
		String result = "";
		if(list.size() < 2) {
			logger.warning("This function requires two parametes <Language> and <Text>");
			if(list.size() == 1) {
				result = list.get(0).getStrData();
			} 
		} else {
			result = Dictionary.getInstance().translate(list.get(0).getStrData(),list.get(1).getStrData());
		}
		returnRowsBuilder.addRows(Row.newBuilder().addDuals(Dual.newBuilder().setStrData(result).build()).build());		
	}

	@Override
	public BundledRows getReturnRows() {
		return returnRowsBuilder.build();
	}

}
