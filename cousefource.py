# coding=utf-8
import urllib2,sys,urllib,string
import MySQLdb,os
import re
from HTMLParser import HTMLParser
class MyHTMLParser(HTMLParser):
    def __init__(self):
        HTMLParser.__init__(self)
        self.links=[]
        self.count=0
        self.isrecord=0
        self.time=0
        self.week_record=1
        self.monday=['monday']
        self.tuesday=['tuesday']
        self.wednesday=['wednesday']
        self.thursday=['thursday']
        self.friday=['friday']
        self.saturday=['saturday']
        self.sunday=['sunday']
        self.exam=['exam']

        self.week={'week_monday':self.monday,
                   'week_tuesday':self.tuesday,
                   'week_wednesday':self.wednesday,
                   'week_thursday':self.thursday,
                   'week_friday':self.friday,
                   'week_saturday':self.saturday,
                   'week_sunday':self.sunday,
                   'week_exam':self.exam
                   }
        self.tr_count=0
        self.tags=''
        self.record_data=''
        self.havespan=0 
        self.tmp=''
        self.flag=0 
        self.iswrite=0 
        self.number=0
    def handle_starttag(self,tag,attrs):
        if tag=='table':
            self.count+=1
            if self.count>4 and self.count<40:
                self.isrecord=1
                self.week_record=(self.count-4)%7
                self.number=(self.count-4)/7+1
                if self.count==40:
                    self.week_record=7
        if tag=='span':
            self.tags='span'
            self.havespan=1
            self.flag=1   
            
    def handle_data(self,data):
        dat=data.decode("gb2312").encode("utf-8")
        p=re.compile("周考试")
        m=p.search(dat)
       # print m
        if m!=None:
            #print m
            return
        if self.flag==0:
            return
        if self.tags=='span' and self.isrecord==1 :
            if self.time==0:
                x_list=data.split(' ')
                data=''.join(x_list)
            if self.time==1:
                x_list=data.split('  ')
                data=''.join(x_list)
            self.record_data=self.tmp+data #标住第几节课
            #print self.record_data 
            self.tmp=self.record_data+' '
            self.time=self.time+1  
            if self.time>2:
                self.time=0
                self.tmp=''
                self.record_data=str(self.number)+' '+self.record_data
                if self.week_record==1:
                    self.week['week_monday'].append(self.record_data)
                elif self.week_record==2:
                    self.week['week_tuesday'].append(self.record_data)
                elif self.week_record==3:
                    self.week['week_wednesday'].append(self.record_data)
                elif self.week_record==4:
                    self.week['week_thursday'].append(self.record_data)
                elif self.week_record==5:
                    self.week['week_friday'].append(self.record_data)
                elif self.week_record==6:
                    self.week['week_saturday'].append(self.record_data)
                elif self.week_record==0:
                    self.week['week_sunday'].append(self.record_data)
                elif self.week_record==7:
                    self.week['week_exam'].append(self.record_data)

    def handle_endtag(self,tag):
        
        if tag=='table':
            self.isrecord=0
            self.tags=''
            self.time=0
            
        elif tag=='span':
            self.flag=0
  
    def gettitle(self):
        return self.week
    
req=urllib2.Request("http://xscj.hit.edu.cn/Hitjwgl/XS/kfxqkb.asp")
data=urllib.urlencode([('BH',"0903101")]) 
fd=urllib2.urlopen(req,data)

conn=MySQLdb.connect(host='localhost',user='root',passwd='mysql',charset='gbk')
conn.select_db('db_coursefocus')
cursor=conn.cursor(MySQLdb.cursors.DictCursor)

sql="create table if not exists "+"c"+"0937101"+"(courseNum varchar(7) NOT NULL primary key,courseName varchar(30) NOT NULL ,teacherName varchar(30) NOT NULL,location varchar(16) NOT NULL)"
print sql
cursor.execute(sql)

 
tp=MyHTMLParser()
#fd=open('C:\Users\zdr\Desktop\software.htm','r')
tp.feed(fd.read())
week1=tp.gettitle()
weeknum='0'
count=1
flag=0  
dangz="(单)" 
shuangz="(双)"
dan=dangz.decode("utf-8").encode("gb2312")    #注明一定要转码，不然接下来的操作会有问题
shuang=shuangz.decode("utf-8").encode("gb2312")

for value in week1:
    for str in week1[value]:
        #print str
        str_list=string.split(str,' ')

        if value=='week_monday':
            weeknum='1'
        elif value=='week_tuesday':
            weeknum='2'
        elif value=='week_wednesday':
            weeknum='3'
        elif value=='week_thursday':
            weeknum='4'
        elif value=='week_friday':
            weeknum='5'
        elif value=='week_saturday':
            weeknum='6'
        elif value=='week_sunday':
            weeknum='7'
            
        if len(str_list)>1:
            m=str_list[len(str_list)-1]
            if m==dan:
                flag=1
                #print flag     #调试单双周
                m=str_list[len(str_list)-2]
            if m==shuang:
                flag=2
                #print flag     # 调试单双周
                m=str_list[len(str_list)-2]
            m_list=string.split(m,'-')
            #print m,m_list  
            m=m_list[1] 
            h="周" 
            h1=h.decode("utf-8").encode("gb2312") 
            n=string.split(m,h1)

            h='　'
            h1=h.decode("utf-8").encode("gb2312")
            te=string.split(str_list[2],h1)
            print te[0]
            
            if string.atoi(m_list[0])<10:
                m_list[0]='0'+m_list[0]
            if string.atoi(n[0])<10:
                n[0]='0'+n[0]
            tem=m_list[0]+n[0]+weeknum+str_list[0]
            print tem   #学号
           
            sql="insert into "+"c0937101"+"(courseNum,courseName,teacherName,location)values('%s','%s','%s','%s') " %(tem,str_list[1],te[0],te[1])
            cursor.execute(sql)
            print tem
 
conn.commit()
cursor.close()  
conn.close()

          
            
        
 

