import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            Calculator calculator = new Calculator();
            calculator.doStart();

            System.out.println("Input number:");

            try {
                double number = scanner.nextDouble();
                calculator.InputNumberEvent(number);
            } catch (Exception e) {
                System.out.println("Something went wrong!");
            }
            boolean sndLoopBool = true;
            sndLoop:
            while (sndLoopBool) {
                scanner.nextLine();
                System.out.println("Input operation symbol: (eg.: +,-,/,* )");
                String symbol = scanner.nextLine();
                boolean valid = calculator.SymbolValidate(symbol);
                if (!valid) {
                    System.out.println("Invalid input.Press enter to continue...");
                    continue;
                }
                calculator.InputOperationEvent(symbol.charAt(0));
                System.out.println("Input second member:");
                double number;
                try {
                    number = scanner.nextDouble();

                } catch (Exception e) {
                    System.out.println("Please type in a number!");
                    number = scanner.nextDouble();
                }
                calculator.InputNumberEvent(number);
                calculator.ResultEvent();
                loop:
                while (true) {
                    System.out.println("Start new Operation? (1)");
                    System.out.println("Continue Operation? (2)");
                    System.out.println("Exit (3)");
                    int i = scanner.nextInt();
                    switch (i) {
                        case 1:
                            sndLoopBool = false;
                            continue sndLoop;
                        case 2:
                            continue sndLoop;
                        case 3:
                            calculator.doTurnOff();
                        default:
                            break;
                    }
                }

            }
        }
    }
}
