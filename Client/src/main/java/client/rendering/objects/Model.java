package client.rendering.objects;

import java.util.ArrayList;
import java.util.List;

import com.koossa.logger.Log;

import vhacd.VHACD;
import vhacd.VHACDHull;
import vhacd.VHACDParameters;

public class Model {
	
	private List<Mesh> meshes;
	private String[] meshNames;
	private String name;
	private List<VHACDHull> hulls;
	
	public Model(String name, List<Mesh> meshes) {
		this.meshes = meshes;
		this.name = name;
		generateMeshNameList();
	}

	public List<Mesh> getMeshes() {
		return meshes;
	}
	
	public String getName() {
		return name;
	}
	
	private String[] generateMeshNameList() {
		meshNames = new String[meshes.size()];
		for (int i = 0; i < meshes.size(); i++) {
			meshNames[i] = meshes.get(i).getName();
		}
		return meshNames;
	}
	
	public String[] getMeshNames() {
		return meshNames;
	}
	
	private void createVCHADHull() {
		Log.debug(this, "Creating a VHACD Hull for model: " + name);
		 hulls = new ArrayList<VHACDHull>();
		 VHACDParameters p = new VHACDParameters();
		 meshes.forEach(mesh -> {
			 hulls.addAll(VHACD.compute(mesh.getVertices(), mesh.getIndices(), p));
		 });
	}
	
	public List<VHACDHull> getConvexHulls() {
		if (hulls == null || hulls.size() <=0 ) createVCHADHull();
		return hulls;
	}

}
