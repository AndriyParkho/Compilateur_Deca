FROM centos:7
LABEL maintainer="Patrick.Reignier@imag.fr"
RUN yum install -y epel-release 
RUN yum install java-latest-openjdk-devel-14.0.2.12-1.rolling.el7.x86_64 make gcc-gnat.x86_64 maven git which time sudo wget -y
RUN alternatives --set java java-latest-openjdk.x86_64 && alternatives --set javac java-latest-openjdk.x86_64
RUN yum clean all
RUN mkdir /usr/local/ima
COPY ima_sources.tgz /usr/local/ima
RUN cd /usr/local/ima && tar xzvf ima_sources.tgz
RUN localedef -c -f UTF-8 -i en_US en_US.UTF-8
ENV LC_ALL en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en 
RUN useradd -ms /bin/bash gl
RUN usermod -aG wheel gl
RUN echo gl:gl | chpasswd
USER gl
COPY bashrc /home/gl/.bashrc
RUN mkdir /home/gl/Projet_GL
VOLUME /home/gl/Projet_GL
WORKDIR /home/gl/Projet_GL
CMD ["/bin/bash"]