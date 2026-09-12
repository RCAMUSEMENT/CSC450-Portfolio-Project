# CSC450: Programming III — Portfolio Project (Part 2)

**Student Name:** Ryley Carlson
**Course:** CSC450 - Programming III (Module 8)
**Development Environment:** Visual Studio Code (Extension Pack for Java)
**Compiler Toolchain:** Java Development Kit (JDK 17 or Higher)

---

## Project Overview

This repository contains the complete deliverables for the Module 8 Portfolio Project (Part 2) assignment. The project focuses on multi-threaded scheduling synchronization, race-condition elimination, and memory-managed virtual machine optimization:

1. **CONCURRENT SUBMERSIBLE TELEMETRY SIMULATOR (`MainSubmersible.java`):** A concurrent Java console application stylized as a deep-sea diving monitoring dashboard. The application is engineered to spawn two independent worker threads acting as coordinated counters to handle ballast tank flooding and hydrostatic pressure venting safely.
2. **System Design Code Documentation:** Detailed execution analysis mapping out robust thread state variables, explicit `ReentrantLock` mutual exclusion scopes, and `Condition` barrier loop predicate validations to prevent data race conditions and state corruption.

---

## Hardware Optimization Note

While Eclipse IDE was originally recommended for this course, it utilizes a heavy GUI runtime framework that creates an overwhelming processing overhead on integrated graphics processing units. To mitigate UI thread blocking, asset delay bottlenecks, and local development system lag, this entire project environment was migrated to a highly optimized Visual Studio Code workspace configuration. This lean environment ensures that delayed cosmetic scrolling animations run smoothly without stalling background data-handling buffers.

---

## Compilation and Execution Instructions

To compile and run the source file manually via your local native terminal framework, execute the following Java runtime commands:

### 1. Submersible Telemetry Bytecode Compilation
```bash
javac MainSubmersible.java
java MainSubmersible
```

---

## Identified Bug Fixes & Security Enhancements

### MainSubmersible.java Features:

* **Eliminated Core Context Switching Race Conditions:** Patched standard cross-thread timing hazards by wrapping shared variables inside strict `hullLock.lock()` and `try-finally` blocks, completely preventing threads from executing raw split-write operations on the pressure counter.
* **Mitigated CPU Spurious Wake-up Latencies:** Enforced an explicit `while(!divingPhaseComplete)` predicate loop around the `ascentDescentBarrier.await()` sequence, locking the thread until variables are genuinely true and preventing core cycle drains.
* **Preserved Core-Coherence Thread Queue Stability:** Configured explicit `ReentrantLock` handling backed by the underlying abstract queued synchronizer (AQS) engine layer, preventing background context switches and thread lock contention.
* **Isolated Plaintext Data Memory Remanence Holes:** Stripped out dynamic heap-allocated `java.lang.String` mutations inside processing loops to avoid string pool allocation blocks, passing raw literals straight to a lock-protected console handle to insulate data logs from heap-scraping exposures.