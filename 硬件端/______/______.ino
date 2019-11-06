//放置在房间里的温湿度，光照，控制灯光
#include <SoftwareSerial.h>
#include <PubSubClient.h>
#include <ESP8266WiFi.h>
#include <dht11.h>
#include<DS1302.h>
#include <stdlib.h> 
#define DHT11PIN D1//DHT11模块引脚
dht11 DHT11;
//设置连接wi-fi的参数
const int lightPin=D2;//采集房间内光照强度的引脚
const char *ssid="sjj";                    
const char *password="sjj6191055";                      
const char *host="172.20.10.5";
const int httpPort=61613;
const char* mqtt_username = "sjj";
const char* mqtt_password = "123456";
int lightvalue=0;
int pinRelayLight=D0;
WiFiClient mqtt_client;
PubSubClient client(mqtt_client);
char humidity[50],temperature[50];
DS1302 rtc(D5, D4, D3); // RST, DAT, CLK
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  Serial1.begin(9600);
  pinMode(lightPin,INPUT);
  pinMode(pinRelayLight,OUTPUT);
  digitalWrite(pinRelayLight,HIGH);
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
//   // 设置时间后, 需要注释掉设置时间的代码,并重新烧录一次. 以免掉电重新执行setup中的时间设置函数.
  rtc.halt(false);
  rtc.writeProtect(false);
  rtc.setDOW(TUESDAY);
  rtc.setTime(10,51,00);
  rtc.setDate(20, 4, 2019);

}

void loop() {
   while (!client.connected())//几个非连接的异常处理
    {  
    reconnect();
    }
    client.loop();
  // put your main code here, to run repeatedly:
  DHT11.read(DHT11PIN);
  Serial.print("温度值：℃");
  Serial.println(DHT11.temperature);
  itoa(DHT11.temperature,temperature,10);
  client.publish("Temperature190418101",temperature);
  Serial.print("湿度值:%");
  Serial.println(DHT11.humidity);
  itoa(DHT11.humidity,humidity,10);
  client.publish("Humidity190418101",humidity);
  delay(5000);//每间隔5秒发送一次
  lightvalue=digitalRead(lightPin);
  Serial.println("光照强度的值");
  Serial.println(lightvalue);
  //如果光照弱并且时间在白天后面加入时钟模块则打开灯。
  char* times=rtc.getTimeStr();
  char* inhour=strtok(times,":");
  Serial.print("分割出来的时间是:");
  Serial.println(inhour);
  int judgeresult=judgeIsDay(inhour);//判断所处的时间是白天还是晚上，假设18：00~6：00为晚上，不需开灯
  if(lightvalue==1&&judgeresult==1){//时间判断的还没有做
    //如果光照变弱并且在白天则打开灯
    digitalWrite(pinRelayLight,LOW);
  }else{
    digitalWrite(pinRelayLight,HIGH);
  }
  getdatetime();

}
void reconnect(){
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect(mqtt_username, mqtt_username, mqtt_password)) {
      Serial.println("connected");
   
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(1000);
    }
  }
  }
  //回调函数显示结果
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
}
void getdatetime(){
  Serial.println(rtc.getDateStr(FORMAT_LONG, FORMAT_LITTLEENDIAN, '/'));//年月日
  Serial.print(rtc.getDOWStr()); //星期几
  Serial.print("    ");
  Serial.println(rtc.getTimeStr());//时间
}
//白天返回1，夜晚返回0
int judgeIsDay(char* inhour){
   int res=atoi(inhour);
   if(res<24&&res>18) return 0;
   else if(res>0&&res<6) return 0;
   else return 1;
}
