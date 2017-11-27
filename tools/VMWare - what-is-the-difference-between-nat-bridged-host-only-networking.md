
# what-is-the-difference-between-nat-bridged-host-only-networking

> Create Time : 2017年3月24日 Ref : https://superuser.com/questions/227505/what-is-the-difference-between-nat-bridged-host-only-networking

* `Host-Only` - Host-only networking creates **a network that is completely contained within the host computer.** Host-only networking provides a network connection between the virtual machine and the host system by using a virtual network adapter that is visible on the host operating system.

* `NAT` - Just like your home network with a wireless router, the VM will be assigned in a separate subnet, like `192.168.6.1` is your host computer , and VM is  `192.168.6.3` , then your VM can access outside network like your host,but no outside access to your VM directly,it's protected.

* `Bridge` - Your VM will be in the same network as your host , if your host IP is `172.16.120.45` then your VM will be `172.15.20.50`. It can be accessed by all computers in your host network.