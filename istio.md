# Istio 설치 방법
Istio 홈페이지 에서 확인한다.
https://istio.io/latest/docs/setup/getting-started/

## Download Istio
* Istio 다운로드
```
curl -L https://istio.io/downloadIstio | sh -
```
* 설치 확인
```
cd istio-1.17.2/bin
sudo cp istioctl /usr/local/bin 
istioctl version
```
## Istio  Install
설치 전에 아래 구성 profile 확인해 본다. 기본적으로 demo profile로 설정하여 설치 한다.
https://istio.io/latest/docs/setup/additional-setup/config-profiles
```
istioctl install --set profile=demo -y
```

## Bookinfo Application 배포
이 예제는 다양한 Istio 기능을 시연하는데 사용되는 4개의 개별 마이크로서비스로 구성된 샘플 애플리케이션을 배포합니다

https://istio.io/latest/docs/examples/bookinfo/

Bookinfo Application은 4개의 개별 마아크로서비스로 나뉩니다.
* productpage. The productpage microservice calls the details and reviews microservices to populate the page.
* details. The details microservice contains book information.
* reviews. The reviews microservice contains book reviews. It also calls the ratings microservice.
* ratings. The ratings microservice contains book ranking information that accompanies a book review.
> 위 내용 확인은 k9s 에서확인 가능하다. 기본 default 네임스페이스에 설정이 되었기 때문에 s 단축키로 확인 가능하다.

마이크로서비스에는 3가지 버전이 있다.
* Version v1 doesn't call the ratings service
* Version v2 calls the ratings service, and displays each rating as 1 to 5 black stars.
* Version v3 calls the ratings service, and displays each rating as 1 to red starts
https://istio.io/latest/docs/examples/bookinfo/noistio.svg


### 애플리케이션 서비스 시작
* 애플리케이션을 호스팅할 네임스페이스에 다음과 같은 레이블을 지정한다. istio-injection=enabled.
```
kubectl label namespace default istio-injection=enabled
```
* 애플리케이션 배포 및 서비스와 파드 확인
```
cd istio-1.17.2
kubectl apply -f samples/bookinfo/platform/kube/bookinfo.yaml
kubectl get services
kubectl get pods
kubectl exec "$(kubectl get pod -l app=ratings -o jsonpath='{.items[0].metadata.name}')" -c ratings -- curl -sS productpage:9080/productpage | grep -o "<title>.*</title>"


```
* k9s 포트포워딩 방법, 
  svc productpage(쉬프트 f) 에서 포트포워딩 후 localhost:9080 접속한다. normal user클릭하여 새로 고침
  k9s에서 pf list 확인

* ingress ip 및 포트 설정
```
kubectl apply -f samples/bookinfo/networking/bookinfo-gateway.yaml

# 게이트웨이가 생성되었는지 확인
k get gateway
```
* 게이트웨이 액세스하기 위한 변수 설정
https://istio.io/latest/docs/tasks/traffic-management/ingress/ingress-control/#determining-the-ingress-ip-and-ports
```
export INGRESS_NAME=istio-ingressgateway
export INGRESS_NS=istio-system
kubectl get svc "$INGRESS_NAME" -n "$INGRESS_NS"
export INGRESS_HOST=$(kubectl -n "$INGRESS_NS" get service "$INGRESS_NAME" -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
export INGRESS_PORT=$(kubectl -n "$INGRESS_NS" get service "$INGRESS_NAME" -o jsonpath='{.spec.ports[?(@.name=="http2")].port}')
export SECURE_INGRESS_PORT=$(kubectl -n "$INGRESS_NS" get service "$INGRESS_NAME" -o jsonpath='{.spec.ports[?(@.name=="https")].port}')
export TCP_INGRESS_PORT=$(kubectl -n "$INGRESS_NS" get service "$INGRESS_NAME" -o jsonpath='{.spec.ports[?(@.name=="tcp")].port}')

export GATEWAY_URL=$INGRESS_HOST:$INGRESS_PORT
## 해당 적용 후 않됨, AWS cloud에선 확인이 가능하다
```
* ingress 설치
```
kubectl apply -f - <<EOF
apiVersion: networking.istio.io/v1alpha3
kind: Gateway
metadata:
  name: httpbin-gateway
spec:
  # The selector matches the ingress gateway pod labels.
  # If you installed Istio using Helm following the standard documentation, this would be "istio=ingress"
  selector:
    istio: ingressgateway
  servers:
  - port:
      number: 80
      name: http
      protocol: HTTP
    hosts:
    - "*"
---
apiVersion: networking.istio.io/v1alpha3
kind: VirtualService
metadata:
  name: httpbin
spec:
  hosts:
  - "*"
  gateways:
  - httpbin-gateway
  http:
  - match:
    - uri:
        prefix: /headers
    route:
    - destination:
        port:
          number: 8000
        host: httpbin
EOF
```

## 결과
http://bookinfo1.127.0.0.1.sslip.io/productpage

