package exception;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LagerverwaltungsException extends RuntimeException implements Serializable{

	private static final long serialVersionUID = 141161380211463941L;
	
	private List<String> result;
	
	public LagerverwaltungsException(String message, List<String>result, Exception ex) {
		super(message,ex);
		this.result = result;
	}
	
	public List<String> getResult() {
		Map<String,String> singleResult = new HashMap<String, String>(this.result.size());
		for (String s : this.result) {
			if (!singleResult.containsKey(s)) {
				singleResult.put(s, s);
			}
			else
				this.result.remove(s);
		}
		
		return this.result;
	}
	
	public void setResult(List<String> result) {
		this.result = result;
	}
	
	@Override
	public String getMessage() {
		String msg = super.getMessage();
		for (String s : result) {
			msg += "\n" + s;
		}
		return msg;
	}
	
	
}
