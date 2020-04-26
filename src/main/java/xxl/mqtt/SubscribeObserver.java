package xxl.mqtt;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class SubscribeObserver implements Observer<Pair<String, MqttMessage>> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Pair<String, MqttMessage> pair) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
