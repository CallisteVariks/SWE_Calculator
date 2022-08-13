import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Calculator implements ICalcController {

    private Timer timer;
    private Timer idleTimer;
    private CalcState calcState = CalcState.OFF;
    private CalcState prevState = CalcState.IDLE;
    private long minutes = TimeUnit.MINUTES.toSeconds(5000);
    private long idleSecs = TimeUnit.SECONDS.toSeconds(10000);

    Calculator() {
        timer = new Timer();
        idleTimer = new Timer();
    }

    private void setCalcState(CalcState newCalcState) {
        prevState = calcState;
        calcState = newCalcState;

        idleTimer = new Timer();
        idleTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (calcState != CalcState.IDLE) {
                    setCalcState(CalcState.IDLE);
                }
                timer = new Timer();

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        eventTimerOff();
                    }
                }, minutes);
            }
        }, idleSecs);
    }

    void InputOperationEvent(char symbol) {
        if (calcState == CalcState.IDLE)
            calcState = prevState;
        switch (calcState) {
            case MEMBER:
                GetOperator(symbol); //Save operator
                setCalcState(CalcState.OPERATION);
                break;
            case OPERATION:
                alert("Operator after another operator!");
                break;
            case RESULT:
                GetOperator(symbol); //Save operator
                setCalcState(CalcState.OPERATION);
                break;
            default:
                break;
        }
    }

    void InputNumberEvent(double member) {
        if (calcState == CalcState.IDLE)
            calcState = prevState;

        switch (calcState) {
            case IDLE:
                GetNumbers(member); //Save
                setCalcState(CalcState.MEMBER);
                break;
            case OPERATION:
                GetNumbers(member);
                setCalcState(CalcState.MEMBER);
                break;
            case MEMBER:
                alert("Cannot input number after already inputting number!");
                break;
            default:
                break;
        }
    }

    void ResultEvent() {
        if (calcState == CalcState.IDLE)
            calcState = prevState;

        switch (calcState) {
            case MEMBER:
                doCalculation();
                setCalcState(CalcState.RESULT);
                break;
            case OPERATION:
                alert("Input number before operation!");
                break;
            case RESULT:
                alert("Input new number or operation!");
                break;
            default:
                break;
        }
    }

    private void alert(String message) {
        System.out.println("Incorrect input! " + message);
    }

    private void eventTimerOff() {
        if (calcState == CalcState.IDLE) {
            setCalcState(CalcState.OFF);
            doTurnOff();
        }
    }

    @Override
    public void doStart() {
        calcState = CalcState.IDLE;

        idleTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                setCalcState(CalcState.IDLE);
            }
        }, idleSecs);
    }

    private ArrayList<String> operations = new ArrayList<>();

    @Override
    public void GetNumbers(double member) {
        operations.add(member + "");
    }

    @Override
    public void GetOperator(char operator) {
        if (operator == '=') {
            alert("This operator is not allowed here");
            return;
        }
        operations.add(operator + "");
    }

    @Override
    public void doCalculation() {
        double result = 0;
        for (int i = 0; i < operations.size(); i++) {

            double member1 = Double.parseDouble(operations.get(i));
            double member2 = Double.parseDouble(operations.get(i + 2));

            switch (operations.get(i + 1).charAt(0)) {
                case '+':
                    result = member1 + member2;
                    break;
                case '-':
                    result = member1 - member2;
                    break;
                case '/':
                    if (member2 == 0) {
                        System.out.println("invalid operation.");
                        member2 = 1;
                    }
                    result = member1 / member2;
                    break;
                case '*':
                    result = member1 * member2;
                    break;
            }

            if (result == Double.POSITIVE_INFINITY || result == Double.NEGATIVE_INFINITY) {
                System.out.println("Infinity");
                doTurnOff();
            }

            operations.clear();
            operations.add(result + "");
            System.out.println(result);
        }
    }

    @Override
    public void doTurnOff() {
        System.exit(1);
    }

    public boolean SymbolValidate(String symbol) {
        char symb = symbol.charAt(0);
        if (symbol.length() > 1) {
            return false;
        } else if (symb == '+' || symb == '*' || symb == '-' || symb == '/') {
            return true;
        } else
            return false;
    }
}
