package site.config;

import site.controller.CfpController;
import site.controller.TicketsController;
import site.model.Branch;

public class Globals {
	public static final Branch CURRENT_BRANCH = Branch.YEAR_2018;
	public static final String PAGE_CFP = CfpController.CFP_CLOSED_JSP;
	public static final String PAGE_TICKETS = TicketsController.TICKETS_END_JSP;
}
