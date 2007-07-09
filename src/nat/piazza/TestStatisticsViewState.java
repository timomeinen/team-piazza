/**
 * 
 */
package nat.piazza;

public class TestStatisticsViewState {
	private final int passed, failed, ignored;
	
	public TestStatisticsViewState(int passedTestCount, int failedTestCount, int ignoredTestCount) {
		passed = passedTestCount;
		failed = failedTestCount;
		ignored = ignoredTestCount;
	}
	
	public boolean getAnyHaveRun() {
		return getCompleted() > 0;
	}
	
	public int getCompleted() {
		return passed + failed + ignored;
	}
	
	public int getFailed() {
		return failed;
	}

	public int getIgnored() {
		return ignored;
	}
	
	public int getPassed() {
		return passed;
	}
}