package Tests;

import Pages.CartPage;
import Pages.HomePage;
import Pages.ProductPage;
import Pages.ResultsPage;
import RetryAnalyzer.RetryAnalyzer;
import Utils.TestConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class AddingProductToCart {
    private static WebDriver driver;
    private HomePage homePage;
    private ResultsPage resultsPage;
    private ProductPage productPage;
    private CartPage cartPage;
    private String productTitle;

    @BeforeTest
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1024,768));

        homePage = new HomePage(driver);
        resultsPage = new ResultsPage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);

        homePage.openURL(TestConfig.BASE_URL);
    }

    @Test (retryAnalyzer = RetryAnalyzer.class)
    public void testCategorySearch(){
        homePage.setSearchTxtBox("car accessories");
        homePage.clickSearchBtn();
    }

    @Test (retryAnalyzer = RetryAnalyzer.class, dependsOnMethods = "testCategorySearch")
    public void testSelectFirstItem(){
        resultsPage.clickProductLink();
    }

    @Test (retryAnalyzer = RetryAnalyzer.class, dependsOnMethods = "testSelectFirstItem")
    public void testAddItemToCart(){
        productTitle = productPage.getProductTitle();
        productPage.clickAddToCartBtn();
    }

    @Test (retryAnalyzer = RetryAnalyzer.class, dependsOnMethods = "testAddItemToCart")
    public void testCheckItemAddedToCart(){
        cartPage.clickCartBtn();
        String cartProductTitle = cartPage.getCartProductTitle();
        Assert.assertEquals(cartProductTitle, productTitle, "The selected item didn't added to the cart");
    }

    @AfterTest
    public void teardown() {
        driver.quit();
    }
}
