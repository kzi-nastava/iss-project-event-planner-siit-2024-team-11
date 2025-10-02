package e2e.tests;

import e2e.base.ChromeTestBase;
import e2e.pages.BudgetOrganizationPage;
import e2e.pages.EventOrganizationPage;
import e2e.pages.LoginPage;
import e2e.pages.ReservationPage;
import org.example.eventy.EventyApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EventyApplication.class)
@ActiveProfiles("test") // if you use application-test.properties
public class BudgetManagementTest extends ChromeTestBase {
    private LoginPage loginPage;
    private BudgetOrganizationPage budgetOrganizationPage;
    private ReservationPage reservationPage;

    @BeforeEach
    public void setupTest() {
        loginPage = new LoginPage(driver);
        loginPage.login("ves@gmail.com", "admin");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/"));
    }

    @AfterEach
    public void tearDownTest() {
        loginPage.logout();
    }

    @Test
    public void enterBudget_IsNotEventOrganizer_ShowError() {
        budgetOrganizationPage = new BudgetOrganizationPage(driver, 2);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/budget/2"));

        assertTrue(budgetOrganizationPage.isErrorDialogOpen());

        budgetOrganizationPage.closeErrorDialog();
    }

    @Test
    public void enterBudget_IsEventOrganizer_ShowBudget() {
        budgetOrganizationPage = new BudgetOrganizationPage(driver, 3);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/budget/3"));

        assertFalse(budgetOrganizationPage.isErrorDialogOpen());
    }

    @Test
    public void addBudgetItem_Successful_ShowBudget() throws InterruptedException {
        budgetOrganizationPage = new BudgetOrganizationPage(driver, 3);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/budget/3"));

        int originalBudgetItemCount = budgetOrganizationPage.getBudgetCategoryItems().size();

        budgetOrganizationPage.clickAddCategoryButton();

        assertTrue(budgetOrganizationPage.isCreateBudgetItemDialogVisible());

        budgetOrganizationPage.selectCreateCategoryCategory("Catering");
        budgetOrganizationPage.setCreateCategoryPlannedFunds("1000");
        budgetOrganizationPage.clickConfirmCreateCategoryButton();

        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".container app-budget-menu-category"), originalBudgetItemCount + 1));

        int newBudgetItemCount = budgetOrganizationPage.getBudgetCategoryItems().size();
        assertEquals(originalBudgetItemCount + 1, newBudgetItemCount);
    }

    @Test
    public void removeBudgetItem_BudgetItemHasReservedItems_ShowError() {
        budgetOrganizationPage = new BudgetOrganizationPage(driver, 3);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/budget/3"));

        budgetOrganizationPage.clickRemoveCategoryButton(0);

        assertTrue(budgetOrganizationPage.isErrorDialogOpen());

        budgetOrganizationPage.closeErrorDialog();
    }

    @Test
    public void removeBudgetItem_Successful_ShowBudget() {
        budgetOrganizationPage = new BudgetOrganizationPage(driver, 3);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/budget/3"));

        budgetOrganizationPage.clickRemoveCategoryButton(budgetOrganizationPage.getBudgetCategoryItems().size() - 1);

        int originalBudgetItemCount = budgetOrganizationPage.getBudgetCategoryItems().size();
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".container app-budget-menu-category"), originalBudgetItemCount - 1));

        assertFalse(budgetOrganizationPage.isErrorDialogOpen());

        int newBudgetItemCount = budgetOrganizationPage.getBudgetCategoryItems().size();
        assertEquals(originalBudgetItemCount - 1, newBudgetItemCount);
    }

    @Test
    public void editBudgetItem_Successful_ShowBudget() {
        budgetOrganizationPage = new BudgetOrganizationPage(driver, 3);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/budget/3"));

        budgetOrganizationPage.clickEditCategoryButton(0);

        assertTrue(budgetOrganizationPage.isEditDialogVisible());

        budgetOrganizationPage.setEditCategoryPlannedFunds("100000");
        budgetOrganizationPage.clickEditCategoryConfirmButton();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("edit-item-form")));

        assertFalse(budgetOrganizationPage.isEditDialogVisible());
        assertFalse(budgetOrganizationPage.isErrorDialogOpen());
    }

    @Test
    public void purchaseProduct_Successful_ShowBudget() {
        budgetOrganizationPage = new BudgetOrganizationPage(driver, 3);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/budget/3"));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".container app-budget-menu-category")));

        int originalCount = budgetOrganizationPage.getReservedSolutionsForCategory(0).size();

        budgetOrganizationPage.clickAddSolutionButton(0);

        assertTrue(budgetOrganizationPage.isSolutionSelectionDialogVisible());

        budgetOrganizationPage.clickSelectProductsButton();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("mat-dialog-content .solution-card")));

        budgetOrganizationPage.clickGetSpecificSolution(0);
        budgetOrganizationPage.clickPurchaseProductButton();

        assertFalse(budgetOrganizationPage.isErrorDialogOpen());

        int newCount = budgetOrganizationPage.getReservedSolutionsForCategory(0).size();

        assertEquals(originalCount + 1, newCount);
    }

    @Test
    public void removeProduct_Successful_ShowBudget() {
        budgetOrganizationPage = new BudgetOrganizationPage(driver, 3);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/budget/3"));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".container app-budget-menu-category")));

        int originalCount = budgetOrganizationPage.getReservedSolutionsForCategory(0).size();

        budgetOrganizationPage.clickRemoveSolutionButton(0, 0);

        assertFalse(budgetOrganizationPage.isErrorDialogOpen());

        int newCount = budgetOrganizationPage.getReservedSolutionsForCategory(0).size();

        assertEquals(originalCount - 1, newCount);
    }

    @Test
    public void reserveService_Successful_ShowBudget() {
        budgetOrganizationPage = new BudgetOrganizationPage(driver, 3);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/budget/3"));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".container app-budget-menu-category")));

        int originalCount = budgetOrganizationPage.getReservedSolutionsForCategory(1).size();

        budgetOrganizationPage.clickAddSolutionButton(1);

        assertTrue(budgetOrganizationPage.isSolutionSelectionDialogVisible());

        budgetOrganizationPage.clickSelectServicesButton();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("mat-dialog-content .solution-card")));

        budgetOrganizationPage.clickGetSpecificSolution(0);

        reservationPage = new ReservationPage(driver);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("app-event-card")));

        reservationPage.clickEventCard();
        reservationPage.clickContinueButton();

        reservationPage.setEventDate("02/18/2026");
        reservationPage.setStartTime("24:00");
        reservationPage.setEndTime("2:00");

        reservationPage.clickConfirmReservationButton();

        assertTrue(reservationPage.isYesNoDialogVisible());
        reservationPage.clickYesNoDialogConfirmButton();

        WebDriverWait longerWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        longerWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".successful_dialog")));

        assertTrue(reservationPage.isSuccessfulDialogVisible());

        budgetOrganizationPage = new BudgetOrganizationPage(driver, 3);
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/budget/3"));

        int newCount = budgetOrganizationPage.getReservedSolutionsForCategory(1).size();

        assertEquals(originalCount + 1, newCount);
    }

    @Test
    public void removeService_Successful_ShowBudget() {
        budgetOrganizationPage = new BudgetOrganizationPage(driver, 3);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/budget/3"));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".container app-budget-menu-category")));

        int originalCount = budgetOrganizationPage.getReservedSolutionsForCategory(1).size();

        budgetOrganizationPage.clickRemoveSolutionButton(1, 0);

        assertFalse(budgetOrganizationPage.isErrorDialogOpen());

        int newCount = budgetOrganizationPage.getReservedSolutionsForCategory(0).size();

        assertEquals(originalCount - 1, newCount);
    }

    @Test
    public void removeService_Unsuccessful_ShowError() {
        budgetOrganizationPage = new BudgetOrganizationPage(driver, 3);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.urlToBe("http://localhost:4200/budget/3"));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".container app-budget-menu-category")));

        int originalCount = budgetOrganizationPage.getReservedSolutionsForCategory(0).size();

        budgetOrganizationPage.clickRemoveSolutionButton(1, originalCount - 1);

        assertTrue(budgetOrganizationPage.isErrorDialogOpen());

        budgetOrganizationPage.closeErrorDialog();
    }
}
