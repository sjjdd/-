#include <SoftwareSerial.h>
#include <PubSubClient.h>
#include <ESP8266WiFi.h>
#include <dht11.h>
#include<stdlib.h>
#include<string.h>
const char *ssid="sjj";                    
const char *password="sjj6191055";                      
const char *host="172.20.10.5";
const int httpPort=61613;
const char* mqtt_username = "admin";
const char* mqtt_password = "password";
WiFiClient mqtt_client;
int pinsoilReLay=D1;//土壤湿度浇水继电器
double globalsoil=2000;//全局变量存储浇水值
int jbj=0;//全局变量存储开关状态，一开始为0，一旦接收到开关控制信息，则变为1，开关优先，过30s恢复0。
unsigned long previousMillis=0;//定义之前的时间
const long interval=1000*60;//时间间隔
char msg[50];
PubSubClient client(mqtt_client);
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  pinMode(pinsoilReLay,OUTPUT);
  //先设置继电器为断开
  digitalWrite(pinsoilReLay,HIGH);
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

void loop() {
   while (!client.connected())//几个非连接的异常处理
    {  
    reconnect();
    }
    client.loop();
  // put your main code here, to run repeatedly:
  int soilHumvalue=analogRead(soilHumvalue);
  Serial.print("土壤湿度值");
  Serial.println(soilHumvalue);
  itoa(soilHumvalue,msg,10);//实时地发布土壤湿度的值
  client.publish("SoilHum190418202",msg);
  delay(1000);
 }


void reconnect(){
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect("plant2", mqtt_username, mqtt_password)) {
      Serial.println("connected");
      //去实时地订阅土壤湿度阈值
      client.subscribe("setwaterparam190418202");
      client.subscribe("SoilHum190418202");
      client.subscribe("soil190418202");
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
   //记录程序执行到此处的时间
    unsigned long currentMillis=millis();
    if(currentMillis-previousMillis>=interval){
      previousMillis=currentMillis;
      jbj=0;//恢复0
    }
  if(strcmp(topic,"setwaterparam190418202")==0)
  {
    globalsoil=atof(inchars.c_str());
  }

   if(strcmp(topic,"soil190418202")==0){
     if(inchars=="1")
    {
       digitalWrite(pinsoilReLay,LOW);
       jbj=1;
    }else{
       digitalWrite(pinsoilReLay,HIGH);
       jbj=1;
    }
   }
   if(strcmp(topic,"SoilHum190418202")==0&&jbj==0){
    double value=atof(inchars.c_str());
    if(value>globalsoil)//只有开关权限时间过了之后才恢复自动控制
     digitalWrite(pinsoilReLay,LOW);
     else if(value<=globalsoil)
      digitalWrite(pinsoilReLay,HIGH);
   }
}
