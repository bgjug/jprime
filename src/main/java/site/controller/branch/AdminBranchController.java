package site.controller.branch;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import site.facade.BranchService;
import site.model.Branch;
import site.model.TicketPrice;
import site.model.TicketType;

@Controller
@RequestMapping(value = "/admin/branch")
public class AdminBranchController {

    private static final String ADMIN_BRANCH_EDIT_JSP = "admin/branch/edit";

    private static final String ADMIN_BRANCH_VIEW_JSP = "admin/branch/view";

    private static final String REDIRECT_ADMIN_BRANCH_VIEW_JSP = "redirect:/" + ADMIN_BRANCH_VIEW_JSP;

    private final BranchService branchService;

    public AdminBranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping("/view")
    public String view(Model model, Pageable pageable) {
        Page<Branch> branches = branchService.allBranches(pageable);

        model.addAttribute("branches", branches.getContent());
        model.addAttribute("totalPages", branches.getTotalPages());
        model.addAttribute("number", branches.getNumber());

        return ADMIN_BRANCH_VIEW_JSP;
    }

    @PostMapping("/add")
    public String add(@Valid final BranchForm branchForm, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            initializeModel(model, branchForm);
            return ADMIN_BRANCH_EDIT_JSP;
        }

        Branch branch = branchForm.toBranch();
        List<TicketPrice> ticketPrices = branchForm.toTicketPrices(branch);
        this.branchService.createBranch(branch, ticketPrices);

        return REDIRECT_ADMIN_BRANCH_VIEW_JSP;
    }

    @GetMapping("/add")
    public String edit(Model model) {
        BranchForm branchForm = new BranchForm();
        Branch currentBranch = branchService.getCurrentBranch();
        Map<TicketType, TicketPrice> priceMap = branchService.getTicketPrices(currentBranch);
        BranchForm.updatePrices(priceMap, branchForm);
        initializeModel(model, branchForm);
        return ADMIN_BRANCH_EDIT_JSP;
    }

    @GetMapping("/edit/{year}")
    public String edit(@PathVariable int year, Model model) {
        Branch branch = branchService.findBranchByYear(year);
        Map<TicketType, TicketPrice> priceMap = branchService.getTicketPrices(branch);
        BranchForm branchForm = BranchForm.fromBranchAndPriceMap(branch, priceMap);

        initializeModel(model, branchForm);
        return ADMIN_BRANCH_EDIT_JSP;
    }

    private static void initializeModel(Model model, BranchForm branchForm) {
        model.addAttribute("branch", branchForm);
        model.addAttribute("durations", BranchForm.DURATIONS_MAP.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .toList());
    }

    @GetMapping("/current/{year}")
    public String current(@PathVariable int year) {
        Branch branch = branchService.findBranchByYear(year);
        branchService.setAsCurrent(branch.getLabel());

        return ADMIN_BRANCH_VIEW_JSP;
    }

    @GetMapping("/remove/{year}")
    public String remove(@PathVariable int year) {
        branchService.deleteBranch(year);
        return REDIRECT_ADMIN_BRANCH_VIEW_JSP;
    }

}
