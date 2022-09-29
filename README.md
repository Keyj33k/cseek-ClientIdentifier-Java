<div align="center">

![version](https://img.shields.io/badge/Version-0.0.5-informational?style=flat&logo=&logoColor=white&color=red) ![stars](https://img.shields.io/github/stars/Keyj33k/NetHunt?style=social) ![forks](https://img.shields.io/github/forks/Keyj33k/NetHunt?label=Forks&logo=&logoColor=white&color=blue) ![languages](https://img.shields.io/github/languages/count/Keyj33k/NetHunt?style=social&logo=&logoColor=white&color=blue) ![issues](https://img.shields.io/github/last-commit/Keyj33k/NetHunt?style=flat&logo=&logoColor=white&color=blue) ![platform](https://img.shields.io/badge/Platform-Linux/Windows-informational?style=flat&logo=&logoColor=white&color=green) 

<a href="https://github.com/Keyj33k/cseek-ClientIdentifier/archive/refs/heads/main.zip"><img src="https://github.com/Keyj33k/cseek-ClientIdentifier-Java/blob/main/img/cseek_banner.svg" alt="banner"/></a>
  
</div>

## cseekers Mission
- scan address range in local network to detect reachable devices<br>
- optional port scan config for live hosts to work faster and more efficient<br>
- displays a message when a port is open which is usually vulnerable<br>
- store succeed results to a file for saving a detailed summary<br>

## :rocket: Getting Started: 

1 ) Make sure, you have `Java` installed:
```
java -version
```
2 ) If it isn't installed (Debian/-based):
```
sudo apt-get install openjdk-18-jdk
```
3 ) Clone the repository:
```
git clone https://github.com/Keyj33k/cseek-ClientIdentifier-Java.git
```
4 ) `Run cseek` using the following command:
```
java -jar cseek.jar
```

## Options/Usage
```
syntax: 	 java -jar cseek.jar <ipv4Addr(First Three Octets Only)> <minHost> <maxHost> disable
		 java -jar cseek.jar <ipv4Addr(First Three Octets Only)> <minHost> <maxHost> enable <minPort> <maxPort>

examples:	 java -jar cseek.jar 192.168.2 1 5 disable
				      or
		 java -jar cseek.jar 192.168.2 1 10 enable 50 60
```

<div align="center">
  
### The Output Will Be Stored In A Text File: `/output/cseekResults.txt`

</div>

## 🎬 cseek Demo
<div align="center">
  
![demo](https://github.com/Keyj33k/cseek-ClientIdentifier/blob/main/img/cseekDemo.gif?raw=true)
  
</div>

## My Motivation
In most cases I used a simple bash script to start the recon session to learn about the network of the <br> 
current pentest. It's a really good method to getting started, but for bigger ranges it's nearly <br> 
impossible to keep the overview. If the Bash script located active devices, I had to do a port scan on <br> 
each hosts separately. While this process, the workspace looked very messy and the overview was lost quickly. <br> 
In a nutshell, I wanted to create a tool that would do all these things itself and simply save all active <br> 
devices and their associated open ports to one file. If cseek stored the results, I know where i need to begin <br>
my next steps without loosing much time and keep a clear workspace too. 

## Feedback And Bug Report

If you found a bug, or wanna start a discussion, please use ![Github issues](https://github.com/Keyj33k/cseek-ClientIdentifier/issues). You are also invited to <br>
send an email to the following address: `K3yjeek@proton.me`

## LICENSE
```
Copyright (c) 2022 Keyjeek

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

<div align="center">

### Tested on 5.15.0-47-generic-Ubuntu and Win10-21H2-Build-19044.1889

</div>

---




