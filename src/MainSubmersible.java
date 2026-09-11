import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public final class MainSubmersible {
    // Independent locks to guarantee atomic standard console output operations
    private static final ReentrantLock consoleIOMutex = new ReentrantLock();

    // Mutual exclusion lock and condition barrier for shared state variables
    private static final ReentrantLock hullLock = new ReentrantLock();
    private static final Condition ascentDescentBarrier = hullLock.newCondition();

    // Shared operational telemetry fields
    private static int depthPressurePSI = 0;
    private static boolean divingPhaseComplete = false;

    public static void main(String[] args) {
        consoleIOMutex.lock();
        try {
            System.out.println("=========================================================");
            System.out.println("  LAUNCHING SUBMERSIBLE CONCURRENT TELEMETRY SIMULATOR  ");
            System.out.println("=========================================================");
        } finally {
            consoleIOMutex.unlock();
        }

        // Instantiating concurrent execution paths via explicit lambda references
        final Thread diveThread = new Thread(MainSubmersible::executeBallastDive);
        final Thread ascentThread = new Thread(MainSubmersible::executeBallastAscent);

        diveThread.start();
        ascentThread.start();

        try {
            // Block main thread until both worker execution paths terminate cleanly
            diveThread.join();
            ascentThread.join();
        } catch (InterruptedException exception) {
            consoleIOMutex.lock();
            try {
                System.err.println("Main application processing loop interrupted: " + exception.getMessage());
            } finally {
                consoleIOMutex.unlock();
            }
            Thread.currentThread().interrupt();
        }

        consoleIOMutex.lock();
        try {
            System.out.println("=========================================================");
            System.out.println("  MISSION-CRITICAL SYSTEMS TERMINATED CLEANLY            ");
            System.out.println("=========================================================");
        } finally {
            consoleIOMutex.unlock();
        }
    }

    private static void executeBallastDive() {
        for (int psiIndex = 1; psiIndex <= 20; psiIndex++) {
            hullLock.lock();
            try {
                depthPressurePSI = psiIndex;
            } finally {
                hullLock.unlock();
            }

            consoleIOMutex.lock();
            try {
                System.out.println("[Telemetry Thread 1] Ballast Intake Active - Hull Hydrostatic Pressure: "
                                + psiIndex + " PSI");
            } finally {
                consoleIOMutex.unlock();
            }

            try {
                // Brief processing delay to emulate real-world sensor propagation gaps
                TimeUnit.MILLISECONDS.sleep(20);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }

        // Safely update state condition flag and notify the waiting ascent thread
        hullLock.lock();
        try {
            divingPhaseComplete = true;
            ascentDescentBarrier.signalAll();
        } finally {
            hullLock.unlock();
        }
    }

    private static void executeBallastAscent() {
        hullLock.lock();
        try {
            // Predicate loop validation explicitly safeguards against spurious wakeups
            while (!divingPhaseComplete) {
                ascentDescentBarrier.await();
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            return;
        } finally {
            hullLock.unlock();
        }

        while (true) {
            int localCurrentPressure;

            hullLock.lock();
            try {
                if (depthPressurePSI < 0) {
                    break;
                }
                localCurrentPressure = depthPressurePSI;
                depthPressurePSI--;
            } finally {
                hullLock.unlock();
            }

            consoleIOMutex.lock();
            try {
                System.out.println("[Telemetry Thread 2] Ascent Initiated - Blowing Ballast - Hull Hydrostatic Pressure: "
                                + localCurrentPressure + " PSI");
            } finally {
                consoleIOMutex.unlock();
            }

            pauseBallastCycle();
        }
    }

    private static void pauseBallastCycle() {
        try {
            TimeUnit.MILLISECONDS.sleep(25);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}