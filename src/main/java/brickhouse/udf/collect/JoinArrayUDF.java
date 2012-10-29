package brickhouse.udf.collect;

import java.util.List;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;

/**
 *   Return a Ruby-like Array join , appending a separator 
 *     between
 * @author jeromebanks
 *
 */
public class JoinArrayUDF extends GenericUDF {
	private ListObjectInspector listInspector;
	private StringObjectInspector strInspector;

	public String evaluate( List<Object> strArray, String sep) {
		StringBuilder sb= new StringBuilder();
		for(int i=0; i< strArray.size(); ++i ) {
			StringObjectInspector strInspector = (StringObjectInspector) listInspector.getListElementObjectInspector();
			sb.append( strInspector.getPrimitiveJavaObject( strArray.get(i) ));
			if(i <strArray.size() -1 ) {
				sb.append( sep);
			}
		}
		return sb.toString();
	}

	@Override
	public Object evaluate(DeferredObject[] arg0) throws HiveException {
		List objList = listInspector.getList( arg0[0].get());
		return evaluate( objList, strInspector.getPrimitiveJavaObject(arg0[1].get()));
	}

	@Override
	public String getDisplayString(String[] arg0) {
		// TODO Auto-generated method stub
		return "join_array()";
	}

	@Override
	public ObjectInspector initialize(ObjectInspector[] arg0)
			throws UDFArgumentException {
		
		 /// TODO  add default separator
		//// TODO  complain about usage ..
		listInspector = (ListObjectInspector) arg0[0];
		strInspector= (StringObjectInspector) arg0[1];
		
		
		return PrimitiveObjectInspectorFactory.javaStringObjectInspector;
	}
}
