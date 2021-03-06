const app = require('../../main.js');
const chai = require('chai');
const expect = chai.expect;
var event, context;

describe('Tests index', function () {
  it.only('verifies success completes', async () => {
    let success = () => Promise.resolve("Woot")
    let circuit = new app.CircuitBreaker(1, "30 seconds", 1, "30 seconds")

    let result1 = await circuit.protect(success)//.catch(error => alert(error.message))
    expect(result1).to.equal("Woot")
  });

  it.only('verifies error fails', async () => {
    let err = async () => {
      return Promise.reject(new Error("fail"))
    }

    let circuit = new app.CircuitBreaker(1, "30 seconds", 1, "30 seconds")

    let result1 = await circuit.protect(err).catch(err => err)
    expect(result1.message).to.equal("fail")
  });

  it.only('verifies error on circuit open', async () => {
    let err = async () => {
      return Promise.reject(new Error("fail"))
    }
    let success = () => Promise.resolve("Woot")

    let circuit = new app.CircuitBreaker(1, "30 seconds", 1, "30 seconds")

    let result1 = await circuit.protect(err).catch(err => err)
    let result2 = await circuit.protect(success).catch(err => err)
    const regex = /^Execution rejected/
    expect(regex.test(result2.message)).to.be.true;
  });


  // it.only('verifies error on circuit open 1', async () => {
  //   let err = async () => {
  //     return Promise.reject(new Error("fail"))
  //   }
  //   // let err2 = async () => {
  //   //   Promise.reject(new Error("fail2"))
  //   // }
  //   // let success = () => Promise.resolve("Woot")

  //   let circuit = new app.CircuitBreaker(1, "30 seconds", 1, "30 seconds")
  //   // let a = new app.CircuitBreaker(1, "30 seconds", 1, "30 seconds")

  //   // let result1 = await circuit.protect(success)//.catch(error => alert(error.message))
  //   let result1 = await circuit.protect(err).catch(error => { return error.message })
  //   // let result3 = await circuit.protect(success)
  //   // let result2 = circuit.protect(err2).catch(error => alert(error.message))

  //   // const result2 = circuit.protect(() => new Promise(function(success, error) {
  //   //   if (true) {
  //   //     success("Stuff worked!");
  //   //   }
  //   //   else {
  //   //       error(Error("It broke"));
  //   //   }
  //   // }))


  //   // expect(result1).to.be.undefined
  //   expect(result1).to.be.an('Error')//equal("Woot")
    
  //     // .catch(function(e) {
  //     //   expect(e.message).to.equal('fail 2')
  //     // })
  //   // expect(result2).to.be.an("error")
  //       // const result = await app.lambdaHandler(event, context)

  //       // expect(result).to.be.an('object');
  //       // expect(result.statusCode).to.equal(200);
  //       // expect(result.body).to.be.an('string');

  //       // let response = JSON.parse(result.body);

  //       // expect(response).to.be.an('object');
  //       // expect(response.message).to.be.equal("hello world");
  //       // expect(response.location).to.be.an("string");
  // });

});