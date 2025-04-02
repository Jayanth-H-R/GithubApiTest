package utilities;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.LocalDateTime;

public class ExtentReports implements ITestListener {

    public ExtentSparkReporter sparkReporter; //Used for UI of the report
    public com.aventstack.extentreports.ExtentReports extent; //Used for populating common info on the report
    public ExtentTest test; //creating test case entries and update status of test methods in the reports

    public void onStart(ITestContext context) {
        String dateStamp = LocalDateTime.now().toString().replace(':', '-');
        sparkReporter = new ExtentSparkReporter("./src/main/resources/Reports/testReport.html");
        sparkReporter.config().setDocumentTitle("Api Automation Test Report");
        sparkReporter.config().setReportName("Rest assured");
        sparkReporter.config().setTheme(Theme.DARK);
        extent=new com.aventstack.extentreports.ExtentReports();
        extent.attachReporter(sparkReporter); //attaching the spark report to
//        extent.setSystemInfo("browser", "chrome");
//        extent.setSystemInfo("environment", "local");
        extent.setSystemInfo("Tester", "Jayanth");

    }

    public  void onTestSuccess(ITestResult result) {
        test=extent.createTest(result.getName()); //creates new entry in report
        test.log(Status.PASS,"Test case passed "+ result.getName());
    }

    public void onTestFailure(ITestResult result) {
        test=extent.createTest(result.getName());
        test.log(Status.FAIL,"Test case failed"+ result.getName());
        test.log(Status.FAIL, "Test case failed "+result.getThrowable());

    }

    public void onTestSkipped(ITestResult result) {
        test=extent.createTest(result.getName());
        test.log(Status.SKIP,"Test case skip "+ result.getName());
    }

    public void onFinish(ITestContext context) {
        extent.flush();
    }

}
