#FROM tomcat:6
#ENV APP_ROOT /myapp
#RUN apt-get update && apt-get install -y default-jdk
#COPY . $APP_ROOT/
#WORKDIR $APP_ROOT
#RUN jar -cvf myapp.war *
#RUN cp myapp.war $CATALINA_BASE/webapps/myapp.war

FROM tomcat:6
ENV APP_ROOT /myapp
RUN apt-get update && apt-get install -y default-jdk
COPY . $APP_ROOT/
RUN rm -r /usr/local/tomcat/webapps/ROOT
COPY  ROOT.war /usr/local/tomcat/webapps/ROOT.war
#COPY myapp.war /usr/local/tomcat/webapps/myapp.war
WORKDIR $APP_ROOT
