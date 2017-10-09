package blockswarm.network.packets

import java.io.Serializable
import java.util.ArrayList

/**
 *
 * @author cal
 */
class FileListRequestPacket(val ignore: ArrayList<String>) : Serializable
