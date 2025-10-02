package e2e.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BudgetOrganizationPage {
    private WebDriver driver;
    private static final String BASE_URL = "http://localhost:4200/budget/";

    @FindBy(css = ".container app-budget-menu-category")
    private List<WebElement> budgetCategoryItems;

    @FindBy(css = ".add-category-container")
    private WebElement addCategoryButton;

    @FindBy(id = "edit-item-form")
    private WebElement editCategoryDialog;

    @FindBy(id = "edit-item-allocated-funds")
    private WebElement editCategoryPlannedFundsInputBox;

    @FindBy(id = "edit-item-confirm-button")
    private WebElement editCategoryConfirmButton;

    @FindBy(id = "create-item-form")
    private WebElement createCategoryDialog;

    @FindBy(id = "category-selector")
    private WebElement createCategorySelectBox;

    @FindBy(id = "create-item-allocated-funds")
    private WebElement createCategoryPlannedFundsInputBox;

    @FindBy(id = "create-item-confirm-button")
    private WebElement createCategoryConfirmButton;

    @FindBy(css = ".error_dialog")
    private WebElement errorDialog;

    @FindBy(id = "error_dialog_close_button")
    private WebElement errorDialogCloseButton;

    @FindBy(id = "solution-selection-dialog")
    private WebElement solutionSelectionDialog;

    @FindBy(id = "select-products-button")
    private WebElement selectProductsButton;

    @FindBy(id = "select-services-button")
    private WebElement selectServicesButton;

    @FindBy(css = "mat-dialog-content .solution-card")
    private List<WebElement> selectableSolutions;

    @FindBy(id = "budget-purchase-product-button")
    private WebElement purchaseProductButton;

    public BudgetOrganizationPage(WebDriver driver, int eventId) {
        this.driver = driver;
        String pageUrl = BASE_URL + eventId;
        driver.get(pageUrl);
        PageFactory.initElements(driver, this);
    }

    public List<WebElement> getBudgetCategoryItems() {
        return budgetCategoryItems;
    }

    public void clickAddCategoryButton() {
        addCategoryButton.click();
    }

    public void clickRemoveCategoryButton(int index) {
        if (index >= 0 && index < budgetCategoryItems.size()) {
            WebElement categoryItem = budgetCategoryItems.get(index);
            WebElement removeButton = categoryItem.findElement(By.cssSelector(".delete-button"));
            removeButton.click();
        }
    }

    public void clickEditCategoryButton(int index) {
        if (index >= 0 && index < budgetCategoryItems.size()) {
            WebElement categoryItem = budgetCategoryItems.get(index);
            WebElement editButton = categoryItem.findElement(By.cssSelector(".edit-button"));
            editButton.click();
        }
    }

    public void clickAddSolutionButton(int index) {
        if (index >= 0 && index < budgetCategoryItems.size()) {
            WebElement categoryItem = budgetCategoryItems.get(index);
            WebElement addButton = categoryItem.findElement(By.cssSelector(".add-new-solution-container"));
            addButton.click();
        }
    }

    public void setEditCategoryPlannedFunds(String plannedFunds) {
        editCategoryPlannedFundsInputBox.clear();
        editCategoryPlannedFundsInputBox.sendKeys(plannedFunds);
    }

    public void clickEditCategoryConfirmButton() {
        editCategoryConfirmButton.click();
    }

    public boolean isCreateBudgetItemDialogVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.visibilityOf(createCategoryDialog));
            return createCategoryDialog.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void selectCreateCategoryCategory(String category) {
        createCategorySelectBox.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        By optionLocator = By.xpath("//mat-option/span[contains(text(),'" + category + "')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));

        driver.findElement(optionLocator).click();
    }

    public void setCreateCategoryPlannedFunds(String plannedFunds) {
        createCategoryPlannedFundsInputBox.sendKeys(plannedFunds);
    }

    public void clickConfirmCreateCategoryButton() {
        createCategoryConfirmButton.click();
    }

    public boolean isEditDialogVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.visibilityOf(editCategoryDialog));
            return editCategoryDialog.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isErrorDialogOpen() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.visibilityOf(errorDialog));
            return errorDialog.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void closeErrorDialog() {
        errorDialogCloseButton.click();
    }

    public List<WebElement> getReservedSolutionsForCategory(int index) {
        if (index >= 0 && index < budgetCategoryItems.size()) {
            WebElement categoryItem = budgetCategoryItems.get(index);
            return categoryItem.findElements(By.cssSelector(".solution-card"));
        }
        return new ArrayList<WebElement>();
    }

    public void clickRemoveSolutionButton(int categoryIndex, int solutionIndex) {
        if (categoryIndex >= 0 && categoryIndex < budgetCategoryItems.size()) {
            WebElement categoryItem = budgetCategoryItems.get(categoryIndex);
            WebElement reservedSolution = categoryItem.findElements(By.cssSelector(".solution-card")).get(solutionIndex);
            WebElement removeButton = reservedSolution.findElement(By.cssSelector(".delete-button"));
            removeButton.click();
        }
    }

    public boolean isSolutionSelectionDialogVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.visibilityOf(solutionSelectionDialog));
            return solutionSelectionDialog.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isSolutionSelectionDialogInvisible() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.invisibilityOf(solutionSelectionDialog));
            return solutionSelectionDialog.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void clickSelectProductsButton() {
        selectProductsButton.click();
    }

    public void clickSelectServicesButton() {
        selectServicesButton.click();
    }

    public void clickGetSpecificSolution(int index) {
        if (index >= 0 && index < selectableSolutions.size()) {
            WebElement selectableSolution = selectableSolutions.get(index);
            WebElement getButton = selectableSolution.findElement(By.cssSelector(".proceed-button"));
            getButton.click();
        }
    }

    public void clickPurchaseProductButton() {
        purchaseProductButton.click();
    }
}
