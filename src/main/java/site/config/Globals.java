package site.config;

import site.controller.CfpController;
import site.controller.TicketsController;
import site.model.Branch;

public class Globals {

	private Globals(){
	}

	public static final Branch CURRENT_BRANCH = Branch.YEAR_2023;

	public static final String PAGE_TICKETS = TicketsController.TICKETS_REGISTER_JSP;
}
