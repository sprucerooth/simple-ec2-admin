package sprucerooth.aws.ec2.simpleadmin

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.io.BufferedReader
import java.io.InputStreamReader

const val SHELL_START_EC2 = "aws ec2 start-instances"
const val SHELL_STOP_EC2 = "aws ec2 stop-instances"
const val SHELL_EC2_STATUS = "aws ec2 describe-instances"
const val ARG_INSTANCE_ID = " --instance-ids "

@RestController
@RequestMapping("aws/ec2/{instanceId}")
@CrossOrigin
class McController {

    @PostMapping("start")
    fun startServer(@PathVariable instanceId: String) {
        Runtime.getRuntime().exec(SHELL_START_EC2 + ARG_INSTANCE_ID + instanceId)
    }

    @PostMapping("stop")
    fun stopServer(@PathVariable instanceId: String) {
        Runtime.getRuntime().exec(SHELL_STOP_EC2 + ARG_INSTANCE_ID + instanceId)
    }

    @GetMapping("status", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun serverStatus(@PathVariable instanceId: String): String {
        val process = Runtime.getRuntime().exec(SHELL_EC2_STATUS + ARG_INSTANCE_ID + instanceId)
        process.waitFor()

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val stringBuilder = StringBuilder()
        var line: String? = null
        while ({ line = reader.readLine(); line }() != null) {
            stringBuilder.append(line);
            stringBuilder.append(System.getProperty("line.separator"));
        }
        return stringBuilder.toString()
    }
}