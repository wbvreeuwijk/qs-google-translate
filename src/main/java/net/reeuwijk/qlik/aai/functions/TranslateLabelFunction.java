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


public class TranslateLabelFunction implements SSEFunction {
	
	private StringBuffer str = new StringBuffer();
	
	private static final String FUNCTION_NAME = "TranslateLabel";
	
	public static String getParameters() {
		return "Language:String,Text:String";
	}
	
	public static DataType getReturnType() {
		return DataType.STRING;
	}
	
	public static FunctionType getFunctionType() {
		return FunctionType.SCALAR;
	}

	public static String getName() {
		return FUNCTION_NAME;
	}

	@Override
	public void executeRow(Row inRow) {
		List<Dual> list = inRow.getDualsList();
		if(list.size() < 2) {
			logger.warning("This function requires two parametes <Language> and <Text>");
			if(list.size() == 1) {
				str.append(list.get(0).getStrData());
			} 
		} else {
			str.append(Dictionary.getInstance().translate(list.get(0).getStrData(),list.get(1).getStrData()));
		}
	}

	@Override
	public BundledRows getReturnRows() {
		Builder returnRowsBuilder = BundledRows.newBuilder();
		returnRowsBuilder.addRows(Row.newBuilder().addDuals(Dual.newBuilder().setStrData(str.toString()).build()).build());
		return returnRowsBuilder.build();
	}

}
