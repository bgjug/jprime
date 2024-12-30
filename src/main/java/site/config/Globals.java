package site.config;

import site.model.BranchEnum;

/**
 * @deprecated Should be removed together with {@link BranchEnum}
 */
@Deprecated
public final class Globals {

	private Globals(){
	}

	// Don't make changes here anymore.
	// Branch Management is now done using the ADMIN UI
	public static final BranchEnum CURRENT_BRANCH = BranchEnum.YEAR_2025;

}
