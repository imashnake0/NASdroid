package com.boswelja.truemanager.auth.logic.manageservers

import com.boswelja.truemanager.auth.logic.CreateApiKey
import com.boswelja.truemanager.auth.logic.TestServerToken
import com.boswelja.truemanager.auth.logic.then
import com.boswelja.truemanager.core.api.v2.system.SystemV2Api

/**
 * Creates a token for and stores a new server. See [invoke] for details.
 */
class AddNewServer(
    private val systemV2Api: SystemV2Api,
    private val createApiKey: CreateApiKey,
    private val testServerToken: TestServerToken,
    private val storeNewServer: StoreNewServer,
) {

    /**
     * Adds a server with the given [username] and [password] combination.
     */
    suspend operator fun invoke(
        serverName: String,
        serverAddress: String,
        username: String,
        password: String
    ): Result<Unit> =
        createApiKey(
            serverAddress = serverAddress,
            username = username,
            password = password,
            keyName = "TrueManager for TrueNAS"
        ).then { apiKey ->
            invoke(
                serverName = serverName,
                serverAddress = serverAddress,
                token = apiKey
            )
        }


    /**
     * Adds a server with the given [token].
     */
    suspend operator fun invoke(
        serverName: String,
        serverAddress: String,
        token: String
    ): Result<Unit> =
        testServerToken(serverAddress, token)
            .then {
                val actualName = serverName.ifBlank {
                    val systemInfo = systemV2Api.getSystemInfo()
                    systemInfo.systemProduct
                }
                val uid = systemV2Api.getHostId()
                storeNewServer(
                    server = Server(
                        id = uid,
                        name = actualName,
                        url = serverAddress
                    ),
                    token = token,
                )
            }
}
