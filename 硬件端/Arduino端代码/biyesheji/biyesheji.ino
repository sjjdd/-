#include <SoftwareSerial.h>
#include <PubSubClient.h>
#include <ESP8266WiFi.h>
#include <dht11.h>
#include<stdlib.h>
#include<string.h>
#define DHT11PIN D5//DHT11模块引脚
//定义一系列存储相关阈值的全局变量
double globalsoil=0;//存储浇水阈值
//int flagsb=0;//区别水泵关闭状态
int pinBuzzer=D3;//蜂鸣器引脚
int pinRelay=D1;//继电器引脚
int pinRelayLight=D6;//连接灯的继电器的引脚
const int buttonPin=A0;//土壤湿度检测器的引脚
int potPin=D7;//光敏电阻
int inputValue=0;int val=0;
dht11 DHT11;
char msg[50],msg1[50],msg2[50],msg3[50];
//设置连接wi-fi的参数
const char *deviceid="194131";//设备id对应一套硬件需要修改
const char *ssid="test423";                    
const char *password="423423423";                      
const char *host="192.168.31.98";
const int httpPort=61613;
const char* mqtt_username = "sjj1";
const char* mqtt_password = "sjj1";
WiFiClient mqtt_client;
PubSubClient client(mqtt_client);
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  pinMode(pinBuzzer,OUTPUT);
  pinMode(buttonPin,INPUT);
  pinMode(pinRelay,OUTPUT);
  pinMode(pinRelayLight,OUTPUT);
  digitalWrite(pinRelay, HIGH); 
  digitalWrite(pinRelayLight, HIGH); 
  digitalWrite(buttonPin,LOW);
  Serial.println("Connecting to  ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
 
  Serial.println("");
  Serial.println("WiFi connected");  
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());

  client.setServer(host,httpPort);
  client.setCallback(callback);
}
void reconnect(){
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect(mqtt_username, mqtt_username, mqtt_password)) {
      Serial.println("connected");
      client.subscribe("soil194131");   
      client.subscribe("lightparam194131");
      client.subscribe("setwaterparam194131");
      client.subscribe("RTsoil");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(1000);
    }
  }
  }
 void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  
  String inchars = ""; //用于存放接收到的字符串
  for (int i = 0; i < length; i++) {
    inchars += (char)payload[i];
  }
  Serial.print(inchars);
  Serial.println();

  if (inchars=="1"&&(strcmp(topic,"soil194131")==0)) {
   digitalWrite(pinRelay, LOW);          //打开水泵
//   flagsb=1;
  } else if(inchars=="0"&&(strcmp(topic,"soil194131")==0)){
   digitalWrite(pinRelay, HIGH);         //关闭水泵
//   flagsb=0;
  }else if((strcmp(topic,"lightparam194131")==0)){
    double a=atof(inchars.c_str());//将字符串类型转化为double
    if(a<=3){digitalWrite(pinRelayLight, LOW);          //打开灯,这里我设置了光照阈值为3
    }else{
      digitalWrite(pinRelayLight, HIGH);          //关闭灯
      }
  }
//  else if(strcmp(topic,"setwaterparam194131")==0){
//    globalsoil=atof(inchars.c_str());//全局变量更新为浇水阈值
//  }
//  else if(strcmp(topic,"RTsoil")==0){//若低于这个阈值自动浇水否则停止
//    if(atof(inchars.c_str())<globalsoil&&flagsb==0){
//       digitalWrite(pinRelay, LOW);          //打开水泵
//      }else if(atof(inchars.c_str())>=globalsoil&&flagsb==1){
//         digitalWrite(pinRelay, HIGH);          //关闭水泵
//        }
//  }
 
  
}
void loop() {
  //定义一系列要访问的主题名

  while (!client.connected())//几个非连接的异常处理
    {  
    reconnect();
    }
    client.loop();
  int i=0;
  // put your main code here, to run repeatedly:
  DHT11.read(DHT11PIN);
  Serial.print("Humdity(%):");
  Serial.println(DHT11.humidity);
  Serial.print("Temperature(oC):");
   itoa(DHT11.humidity,msg2,10);
   client.publish("Humidity194131", msg2);
  Serial.println(DHT11.temperature);
   itoa(DHT11.temperature,msg3,10);
   client.publish("Temperature194131", msg3);
  inputValue=analogRead(buttonPin);
  Serial.print("土壤湿度值:");
  Serial.println(inputValue);
  itoa(inputValue,msg1,10);
  client.publish("RTsoil",msg1);//发布土壤传感器的值
   val=analogRead(potPin);//读取传感器的模拟值并赋值给val
    Serial.print("光敏电阻:");
   Serial.println(val);//显示val 变量数值
  delay(5000);
  
  //蜂鸣器报警
  if(DHT11.temperature<1){//低温报警，到时候这个值由后端传过来
      while(i<100){
  digitalWrite(pinBuzzer,HIGH);
  delay(3);
  digitalWrite(pinBuzzer,LOW);
  delay(3);
  i++;
  }
 }


}
