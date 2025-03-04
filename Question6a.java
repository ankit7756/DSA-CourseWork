/*
Algorithm Explanation:

1. We need to print numbers in the sequence "0102030405..." up to a given number `n`.
2. We have a `NumberPrinter` class with methods `printZero()`, `printEven(int x)`, and `printOdd(int x)`. 
   - `printZero()` prints 0.
   - `printEven(int x)` prints an even number.
   - `printOdd(int x)` prints an odd number.
3. We create a `ThreadController` class that manages three threads:
   - `ZeroThread` calls `printZero()` to print 0 before each number.
   - `EvenThread` calls `printEven(int x)` to print even numbers.
   - `OddThread` calls `printOdd(int x)` to print odd numbers.
4. To synchronize the threads, we use semaphores:
   - `zeroSemaphore` starts with 1 (allows the zero thread to print first).
   - `oddSemaphore` and `evenSemaphore` start with 0 (blocks number printing until allowed).
5. The execution order:
   - `ZeroThread` prints `0` and signals the next number thread.
   - Depending on the number, either `OddThread` or `EvenThread` prints the number.
   - That thread signals `ZeroThread` to print the next `0`.
6. This ensures proper interleaving of numbers without race conditions.

*/

import java.util.concurrent.Semaphore; // Importing Semaphore for thread synchronization

// Class responsible for printing numbers
class NumberPrinter {
    // Method to print zero
    public void printZero() {
        System.out.print(0);
    }

    // Method to print even numbers
    public void printEven(int x) {
        System.out.print(x);
    }

    // Method to print odd numbers
    public void printOdd(int x) {
        System.out.print(x);
    }
}

// Class that controls the execution of threads to print numbers in sequence
class ThreadController {
    private int n; // Maximum number to print

    // Semaphores to control execution order
    private Semaphore zeroSemaphore = new Semaphore(1); // Controls printing of zero, starts available
    private Semaphore oddSemaphore = new Semaphore(0); // Controls printing of odd numbers, starts locked
    private Semaphore evenSemaphore = new Semaphore(0); // Controls printing of even numbers, starts locked

    // Constructor to initialize the limit
    public ThreadController(int n) {
        this.n = n;
    }

    // Method executed by ZeroThread
    public void zero(NumberPrinter printer) throws InterruptedException {
        for (int i = 1; i <= n; i++) { // Loop from 1 to n
            zeroSemaphore.acquire(); // Acquire lock to print zero
            printer.printZero(); // Print zero

            // Release the appropriate semaphore (odd or even)
            if (i % 2 == 0) {
                evenSemaphore.release(); // Allow even number to print
            } else {
                oddSemaphore.release(); // Allow odd number to print
            }
        }
    }

    // Method executed by EvenThread
    public void even(NumberPrinter printer) throws InterruptedException {
        for (int i = 2; i <= n; i += 2) { // Loop through even numbers
            evenSemaphore.acquire(); // Wait until it's even number's turn
            printer.printEven(i); // Print even number
            zeroSemaphore.release(); // Allow zero to be printed next
        }
    }

    // Method executed by OddThread
    public void odd(NumberPrinter printer) throws InterruptedException {
        for (int i = 1; i <= n; i += 2) { // Loop through odd numbers
            oddSemaphore.acquire(); // Wait until it's odd number's turn
            printer.printOdd(i); // Print odd number
            zeroSemaphore.release(); // Allow zero to be printed next
        }
    }
}

// Main application class
public class Question6a {
    public static void main(String[] args) {
        int n = 10; // Define the upper limit of numbers
        NumberPrinter printer = new NumberPrinter(); // Create a printer instance
        ThreadController controller = new ThreadController(n); // Create a controller instance

        // Thread to print zeros
        Thread zeroThread = new Thread(() -> {
            try {
                controller.zero(printer); // Call zero method
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Thread to print even numbers
        Thread evenThread = new Thread(() -> {
            try {
                controller.even(printer); // Call even method
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Thread to print odd numbers
        Thread oddThread = new Thread(() -> {
            try {
                controller.odd(printer); // Call odd method
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Start all threads
        zeroThread.start();
        evenThread.start();
        oddThread.start();
    }
}

/*
 * Testing Results:
 * Input: n = 5
 * Expected Output: 0102030405
 * Explanation:
 * - ZeroThread starts first and prints '0'.
 * - OddThread prints '1', then ZeroThread prints '0' again.
 * - EvenThread prints '2', then ZeroThread prints '0' again.
 * - OddThread prints '3', then ZeroThread prints '0' again.
 * - EvenThread prints '4', then ZeroThread prints '0' again.
 * - OddThread prints '5'.
 * 
 * Final Output: 0102030405
 */