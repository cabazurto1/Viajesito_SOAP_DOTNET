using ec.edu.monster.vista;
using System;
using System.Threading.Tasks;

namespace _02._CLICON
{
    internal class Program
    {
        static async Task Main(string[] args)
        {
            await LoginView.MostrarAsync();
        }
    }
}
