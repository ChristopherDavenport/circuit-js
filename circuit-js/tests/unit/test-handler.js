const app = require('../../circuit-js-opt.js');
const chai = require('chai');
const expect = chai.expect;
var event, context;

describe('Tests index', function () {
  it('verifies successful response', async () => {
    let err = async () => {
      Promise.reject(new Error("Planned Failure"))
    }

    let circuit = new app.CircuitBreaker(1, "30 seconds", 1, "30 seconds")

    const result1 = await circuit.protect(first).catch(error => alert(error.message))


        // const result = await app.lambdaHandler(event, context)

        // expect(result).to.be.an('object');
        // expect(result.statusCode).to.equal(200);
        // expect(result.body).to.be.an('string');

        // let response = JSON.parse(result.body);

        expect(response).to.be.an('object');
        expect(response.message).to.be.equal("hello world");
        // expect(response.location).to.be.an("string");
  });
});