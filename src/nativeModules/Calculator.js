import {NativeModules, NativeEventEmitter} from 'react-native';

const NativeCalculator = NativeModules.Calculator;
const CalculatorEmitter = new NativeEventEmitter(NativeCalculator);

const EventName = NativeCalculator.EVENT_ADD_SUCCESS;

class Calculator {
  native;
  subscription;

  constructor(native) {
    this.native = native;
  }

  async addWithPromise(n1, n2) {
    return await this.native.addWithPromise(n1, n2);
  }

  async addWithCallback(n1, n2, callback) {
    this.native.addWithCallback(n1, n2, callback, (e) => {});
  }

  addWithListener(n1, n2) {
    this.native.addWithListener(n1, n2);
  }

  addResultListener(listener) {
    this.subscription = CalculatorEmitter.addListener(EventName, listener);
  }

  removeResultListener() {
    this.subscription && this.subscription.remove();
  }
}

export default new Calculator(NativeCalculator);
