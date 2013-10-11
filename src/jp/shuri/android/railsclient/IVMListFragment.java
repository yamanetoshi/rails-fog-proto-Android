package jp.shuri.android.railsclient;

import jp.shuri.android.railsclient.VMListFragment.Operation;

public interface IVMListFragment {
	public void kickoff(final boolean b, final int i, Operation ope, final String hostname);
}
