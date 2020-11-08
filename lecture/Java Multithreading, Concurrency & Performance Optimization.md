# Section 1 : Introduction

**Why need Thread?**

- Responsiveness
- Performance

Concurrency == Multitasking

No need multiple cores to achieve concurrency

but multiple cores can truly run tasks completely in parallel

**Impact**

- completing a complex task much faster - performance - concurrency
- Finish more work in the same period of time - responsiveness - parallelism

**Context switching**

- store data for each thread
- thrashing : spending more time in switching than real productive task

**How real CPU scheduling threads**

- divide time by epoch (kind of term)
- Dynamic priority : static priority (by Programmers) + bonus (by OS, can be negative)
    - OS will give preference for preventing starvation

**Multithreaded Architecture Pros**

- share a lot of data
- much faster to create and destory
- switching in same process is faster (than process switching)

**Multi-Process Architecture Pros**

- Security and stability
    - completely isolated