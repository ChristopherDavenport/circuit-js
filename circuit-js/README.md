## circuit-js

How do you use this

```javascript
let circuit = new CircuitBreaker(
  maxFailures: Int, // How many consecutive errors before opening the circuit breaker
  resetTimeout: String, // Like '30 seconds'
  exponentialBackoffFactor: Int  = 1
  maxResetTimeout: String = "5 minutes"
)

circuit.protect(() => fetch('http://example.com/movies.json'))
```