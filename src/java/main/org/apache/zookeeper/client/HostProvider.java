/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.zookeeper.client;

import org.apache.yetus.audience.InterfaceAudience;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

/**
 * A set of hosts a ZooKeeper client should connect to.
 * zooKeeper 客户端应该连接的一组主机
 * 
 * Classes implementing this interface must guarantee the following:
 * 实现这个接口的类必须保证以下内容：
 * * Every call to next() returns an InetSocketAddress. So the iterator never
 * ends.
 *
 * 每次调用next() 之后都必须返回一个InetSocketAddress，所以迭代器永远不会结束
 * 
 * * The size() of a HostProvider may never be zero.
 *
 * HostProvider.size() 可能永远都不会是0
 * 
 * A HostProvider must return resolved InetSocketAddress instances on next(),
 * but it's up to the HostProvider, when it wants to do the resolving.
 *
 * HostProvider必须在next()方法返回已解析的InetSocketAddress
 *
 * Different HostProvider could be imagined:
 * 设想有一堆不同的HostProvider：
 * 
 * * A HostProvider that loads the list of Hosts from an URL or from DNS
 * 从URL或者DNS中加载主机列表
 * * A HostProvider that re-resolves the InetSocketAddress after a timeout.
 * 超时后重新解析InetSocketAddress
 * * A HostProvider that prefers nearby hosts.
 *
 */
@InterfaceAudience.Public
public interface HostProvider {


    int size();

    /**
     * The next host to try to connect to.
     * 尝试连接的下一个主机
     * For a spinDelay of 0 there should be no wait.
     * 若spinDelay为0则不应该等待
     * 
     * @param spinDelay 需要等待的毫秒数 Milliseconds to wait if all hosts have been tried once.
     */
    InetSocketAddress next(long spinDelay);

    /**
     *
     * 通知HostProvider成功连接。HostProvider可以使用此通知来重置其内部状态
     * Notify the HostProvider of a successful connection.
     * The HostProvider may use this notification to reset it's inner state.
     */
    void onConnected();

    /**
     * 更新服务器列表，如果负载均衡需要更改连接，则返回true，否则返回false
     * Update the list of servers. This returns true if changing connections is necessary for load-balancing, false otherwise.
     * @param serverAddresses 新的服务器列表 new host list
     * @param currentHost 此客户端当前连接的服务器 the host to which this client is currently connected
     * @return true if changing connections is necessary for load-balancing, false otherwise  
     */
    boolean updateServerList(Collection<InetSocketAddress> serverAddresses, InetSocketAddress currentHost);
}
