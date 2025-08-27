package iri.elearningapi.utils;

public class URLElearning {
	private String	url		= "/";
	private Object	data	= null;

	public URLElearning() {
		// TODO Auto-generated constructor stub
	}
	
	public URLElearning(String url, Object data) {
		// TODO Auto-generated constructor stub
		this.url=url;
		this.data=data;
	}
	
	public URLElearning(String url) {
		// TODO Auto-generated constructor stub
		this.url=url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
