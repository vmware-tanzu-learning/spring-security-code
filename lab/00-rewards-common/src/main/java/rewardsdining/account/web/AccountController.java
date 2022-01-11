package rewardsdining.account.web;

import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import rewardsdining.account.Account;
import rewardsdining.account.AccountManager;

@Controller
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public class AccountController {

	private final AccountManager accountManager;

	public AccountController(AccountManager accountManager) {
		this.accountManager = accountManager;
	}
	
	@GetMapping("/accounts/{id}")
	public String getAccountDetails(@PathVariable("id") long id, Model model) {
		model.addAttribute("account", accountManager.getAccount(id));
		return "accountDetails";
	}

	@GetMapping("/accounts")
	public String getAccountList(Model model) {
		model.addAttribute("accounts", accountManager.getAllAccounts());
		return "account/list";
	}

	@GetMapping("/editAccount")
	public String getEditAccount(@RequestParam("id") long id, Model model) {
		model.addAttribute("account", this.accountManager.getAccount(id));
		return "editAccount";
	}

	@PostMapping("/editAccount")
	public String postEditAccount(Account account, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("fieldErrors", bindingResult.getFieldErrors().stream().collect(Collectors.groupingBy(FieldError::getField)));
			return "editAccount";
		}
		this.accountManager.update(account);

		return "redirect:/accountDetails";
	}


}
