import { PrismaPg } from '@prisma/adapter-pg';
import { PrismaClient } from '@prisma/client';
import bcrypt from 'bcrypt';

const prisma = new PrismaClient({
  adapter: new PrismaPg({ connectionString: process.env.DATABASE_URL })
});

async function main() {
  const hashedPassword = await bcrypt.hash('admin123', 10);
  await prisma.user.createMany({
    data: [
      {
        name: 'Admin',
        email: 'admin@bms.com',
        password: hashedPassword,
        role: 'admin',
        isActive: true,
      }
    ],
  });

  console.log('🌱 Seed data created');
}

main()
  .catch((e) => {
    console.error(e);
    process.exit(1);
  })
  .finally(async () => {
    await prisma.$disconnect();
  });