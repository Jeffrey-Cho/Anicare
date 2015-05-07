package sep.software.anicare.interfaces;

public interface PairEntityCallback <E, K> {
	public void onCompleted(E firstEntity, K secondEntity);
}
