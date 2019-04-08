package net.reeuwijk.qlik.aai.functions;

import java.util.List;

import net.reeuwijk.qlik.aai.util.SSEFunction;
import net.reeuwijk.qlik.translate.util.StringAnalyzer;
import qlik.sse.BundledRows;
import qlik.sse.BundledRows.Builder;
import qlik.sse.DataType;
import qlik.sse.Dual;
import qlik.sse.FunctionType;
import qlik.sse.Row;

public class AnalyzeRowsFunction implements SSEFunction {

	public AnalyzeRowsFunction() {
	}

	private Builder returnRowsBuilder = BundledRows.newBuilder();

	private static final String FUNCTION_NAME = "AnalyzeRows";

	public static String getParameters() {
		return "Text:String";
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
		StringAnalyzer sa = StringAnalyzer.getInstance();
		String result = "";
		if (list.size() != 1) {
			logger.warning("This function requires one parameter <Text>");
			if (list.size() == 1) {
				result = list.get(0).getStrData();
			}
		} else {
			result = sa.analyzeEntities(list.get(0).getStrData());
		}
		returnRowsBuilder.addRows(Row.newBuilder().addDuals(Dual.newBuilder().setStrData(result).build()).build());
	}

	@Override
	public BundledRows getReturnRows() {
		return returnRowsBuilder.build();

	}

}
