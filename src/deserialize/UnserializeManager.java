package deserialize;

import java.util.ArrayList;

public class UnserializeManager implements Unserialize{
	
	private Unserialize unserialize;
	
	
	
	public UnserializeManager(Unserialize unserialize) {
		this.unserialize = unserialize;
	}



	@Override
	public ArrayList<Object> unserializeImgLog(String path) {
		return unserialize.unserializeImgLog(path);
	}

}
