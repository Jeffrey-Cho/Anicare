package sep.software.anicare.callback;

public interface PairEntityCallback <E, K> {
	public void onCompleted(E firstEntity, K secondEntity);
}
